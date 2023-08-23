package io.github.srcimon.screwbox.core.physics.internal;

import io.github.srcimon.screwbox.core.*;
import io.github.srcimon.screwbox.core.physics.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public class DefaultPhysics implements Physics {

    private final Engine engine;

    private PathfindingAlgorithm algorithm = new AStarAlgorithm();

    private Grid grid = new Grid(Bounds.atOrigin(Vector.zero(), 32, 32), 16);

    public DefaultPhysics(final Engine engine) {
        this.engine = engine;
    }

    @Override
    public RaycastBuilder raycastFrom(final Vector position) {
        return new RaycastBuilder(engine.entities(), position);
    }

    @Override
    public SelectEntityBuilder searchAtPosition(final Vector position) {
        return new SelectEntityBuilder(engine.entities(), position);
    }

    @Override
    public SelectEntityBuilder searchInRange(final Bounds range) {
        return new SelectEntityBuilder(engine.entities(), range);
    }

    @Override
    public Optional<Path> findPath(final Grid grid, final Vector start, final Vector end) {
        final Grid.Node startPoint = grid.toGrid(start);
        final Grid.Node endPoint = grid.toGrid(end);
        if (grid.isBlocked(startPoint) || grid.isBlocked(endPoint)) {
            return Optional.empty();
        }
        final List<Grid.Node> path = algorithm.findPath(grid, startPoint, endPoint);
        if (path.isEmpty()) {
            return Optional.empty();
        }

        final List<Vector> list = new ArrayList<>();
        list.add(start);

        for (final var node : path) {
            list.add(grid.worldPosition(node));
        }

        list.add(end);
        return Optional.of(Path.withNodes(list));
    }

    @Override
    public Optional<Path> findPath(final Vector start, final Vector end) {
        return findPath(grid, start, end);
    }

    @Override
    public Physics setGrid(final Grid grid) {
        this.grid = requireNonNull(grid, "grid must not be null");
        return this;
    }

    @Override
    public Grid grid() {
        return grid;
    }

    @Override
    public Physics setPathfindingAlgorithm(final PathfindingAlgorithm algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    @Override
    public Bounds snapToGrid(final Bounds bounds) {
        requireNonNull(bounds, "bounds must not be null");
        return bounds.moveTo(grid.snap(bounds.position()));
    }

    @Override
    public Vector snapToGrid(final Vector position) {
        requireNonNull(position, "position must not be null");
        if (isNull(grid)) {
            throw new IllegalStateException("no grid present");
        }
        return grid.snap(position);
    }

    @Override
    public PathfindingAlgorithm pathfindingAlgorithm() {
        return algorithm;
    }

}
