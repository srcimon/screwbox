package dev.screwbox.core.navigation.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.navigation.AStarAlgorithm;
import dev.screwbox.core.navigation.Graph;
import dev.screwbox.core.navigation.Grid;
import dev.screwbox.core.navigation.Navigation;
import dev.screwbox.core.navigation.Path;
import dev.screwbox.core.navigation.PathfindingAlgorithm;
import dev.screwbox.core.navigation.RaycastBuilder;
import dev.screwbox.core.navigation.SelectEntityBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

public class DefaultNavigation implements Navigation {

    private final Engine engine;

    private PathfindingAlgorithm<Offset> algorithm = new AStarAlgorithm<>();

    private Grid grid;
    private Graph<Offset> nodeGraph;
    private boolean isDiagonalMovementAllowed = true;

    public DefaultNavigation(final Engine engine) {
        this.engine = engine;
    }

    @Override
    public RaycastBuilder raycastFrom(final Vector position) {
        return new RaycastBuilder(engine.environment(), position);
    }

    @Override
    public SelectEntityBuilder searchAtPosition(final Vector position) {
        return new SelectEntityBuilder(engine.environment(), position);
    }

    @Override
    public SelectEntityBuilder searchInRange(final Bounds range) {
        return new SelectEntityBuilder(engine.environment(), range);
    }

    @Override
    public Navigation setDiagonalMovementAllowed(final boolean isDiagonalMovementAllowed) {
        this.isDiagonalMovementAllowed = isDiagonalMovementAllowed;
        return this;
    }

    @Override
    public boolean isDiagonalMovementAllowed() {
        return isDiagonalMovementAllowed;
    }

    @Override
    public Optional<Path> findPath(final Vector start, final Vector end, final Grid grid) {
        final Offset startPoint = grid.toGrid(start);
        final Offset endPoint = grid.toGrid(end);
        if (grid.isBlocked(startPoint) || grid.isBlocked(endPoint)) {
            return Optional.empty();
        }
        final List<Offset> path = algorithm.findPath(nodeGraph, startPoint, endPoint);
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
        checkGridPresent();
        return findPath(start, end, grid);
    }

    @Override
    public Navigation setGrid(final Grid grid) {
        this.grid = grid;
        nodeGraph = new Graph<>() {

            @Override
            public List<Offset> adjacentNodes(Offset node) {
                return isDiagonalMovementAllowed
                        ? grid.freeSurroundingNodes(node)
                        : grid.freeAdjacentNodes(node);
            }

            @Override
            public double traversalCost(Offset start, Offset end) {
                return start.distanceTo(end);
            }
        };
        return this;
    }

    @Override
    public Optional<Grid> grid() {
        return ofNullable(grid);
    }

    @Override
    public Navigation setPathfindingAlgorithm(final PathfindingAlgorithm algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    @Override
    public PathfindingAlgorithm pathfindingAlgorithm() {
        return algorithm;
    }

    private void checkGridPresent() {
        if (isNull(grid)) {
            throw new IllegalStateException("no grid present");
        }
    }
}
