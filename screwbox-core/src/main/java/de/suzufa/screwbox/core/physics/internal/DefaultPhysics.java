package de.suzufa.screwbox.core.physics.internal;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Grid;
import de.suzufa.screwbox.core.Grid.Node;
import de.suzufa.screwbox.core.Path;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.physics.DijkstraAlgorithm;
import de.suzufa.screwbox.core.physics.PathfindingAlgorithm;
import de.suzufa.screwbox.core.physics.Physics;
import de.suzufa.screwbox.core.physics.RaycastBuilder;
import de.suzufa.screwbox.core.physics.SelectEntityBuilder;

public class DefaultPhysics implements Physics {

    private final ExecutorService executor;
    private final Engine engine;

    private PathfindingAlgorithm algorithm = new DijkstraAlgorithm();

    private Grid grid;

    public DefaultPhysics(final Engine engine, ExecutorService executor) {
        this.engine = engine;
        this.executor = executor;
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

    public PathfindingAlgorithm algorithm() {
        return algorithm;
    }

    public void setAlgorithm(final PathfindingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public Optional<Path> findPath(final Grid grid, final Vector start, final Vector end) {
        final Node startPoint = grid.toGrid(start);
        final Node endPoint = grid.toGrid(end);

        final List<Node> path = algorithm.findPath(grid, startPoint, endPoint);
        if (path.isEmpty()) {
            return Optional.empty();
        }

        final List<Vector> list = new ArrayList<>();
        list.add(start);

        for (var node : path) {
            list.add(grid.worldPosition(node));
        }

        list.add(end);
        return Optional.of(Path.withNodes(list));
    }

    @Override
    public Optional<Path> findPath(final Vector start, final Vector end) {
        if (isNull(grid)) {
            throw new IllegalStateException("no grid for pathfinding present");
        }
        return findPath(grid, start, end);
    }

    @Override
    public Physics setGrid(final Grid grid) {
        this.grid = grid;
        return this;
    }

    @Override
    public Optional<Grid> grid() {
        return ofNullable(grid);
    }

    @Override
    public Physics setPathfindingAlgorithm(final PathfindingAlgorithm algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    @Override
    public Bounds snapToGrid(final Bounds bounds) {
        requireNonNull(bounds, "bounds must not be null");
        Optional<Grid> grid = grid();
        if (grid.isEmpty()) {
            throw new IllegalStateException("no grid present");
        }
        return bounds.moveTo(grid.get().snap(bounds.position()));
    }

    @Override
    public Vector snapToGrid(final Vector position) {
        requireNonNull(position, "position must not be null");
        Optional<Grid> grid = grid();
        if (grid.isEmpty()) {
            throw new IllegalStateException("no grid present");
        }
        return grid.get().snap(position);
    }

    public void shutdown() {
        executor.shutdown();
    }

}
