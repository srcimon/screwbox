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
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DefaultNavigation implements Navigation {

    private final Engine engine;

    private PathfindingAlgorithm<Offset> algorithm = new AStarAlgorithm<>();
    private boolean isDiagonalMovementAllowed = true;
    private int cellSize = 16;
    private GridGraph graph;
    private Grid grid;
    private Bounds navigationRegion;
    private long graphCachingNodeLimit = 40_000;

    public DefaultNavigation(final Engine engine) {
        this.engine = engine;
    }

    @Override
    public Navigation setPathfindingAlgorithm(final PathfindingAlgorithm<Offset> algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    @Override
    public Navigation setNavigationRegion(final Bounds region, final List<Bounds> obstacles) {
        navigationRegion = region.snapExpand(cellSize);
        grid = new Grid(navigationRegion, cellSize);//TODO extend grid to multiples of cell size
        for (final var obstacle : obstacles) {
            grid.blockArea(obstacle);
        }
        updateGraph();
        return this;
    }

    @Override
    public Navigation setGraphCachingNodeLimit(final long nodeLimit) {
        Validate.range(nodeLimit, 0, 10_000_000, "node limit must be in range 0 to 10,000,000");
        this.graphCachingNodeLimit = nodeLimit;
        return this;
    }

    @Override
    public long graphCachingNodeLimit() {
        return graphCachingNodeLimit;
    }

    @Override
    public Bounds navigationRegion() {
        return navigationRegion;
    }

    @Override
    public PathfindingAlgorithm<Offset> pathfindingAlgorithm() {
        return algorithm;
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
        updateGraph();
        return this;
    }

    @Override
    public boolean isDiagonalMovementAllowed() {
        return isDiagonalMovementAllowed;
    }

    @Override
    public Navigation setCellSize(final int cellSize) {
        Validate.range(cellSize, 1, 256, "cell size must be in range from 1 to 256");
        this.cellSize = cellSize;
        updateGraph();
        return this;
    }

    @Override
    public int cellSize() {
        return cellSize;
    }

    @Override
    public Optional<Path> findPath(final Vector start, final Vector end, final Graph<Offset> graph) {
        final Offset startPoint = graph.toGraph(start);
        final Offset endPoint = graph.toGraph(end);
        if (!graph.nodeExists(startPoint) || !graph.nodeExists(endPoint)) {
            return Optional.empty();
        }
        final List<Offset> path = algorithm.findPath(graph, startPoint, endPoint);
        if (path.isEmpty()) {
            return Optional.empty();
        }

        final List<Vector> list = new ArrayList<>();
        list.add(start);

        for (final var node : path) {
            list.add(graph.toPosition(node));
        }

        list.add(end);
        return Optional.of(Path.withNodes(list));
    }

    @Override
    public Optional<Path> findPath(final Vector start, final Vector end) {
        return isNull(graph)
                ? Optional.empty()
                : findPath(start, end, graph);
    }

    private void updateGraph() {
        if (nonNull(grid)) {
            long gridNodes = grid.nodeCount();
            graph = gridNodes <= graphCachingNodeLimit
                    ? new CachedGridGraph(grid, isDiagonalMovementAllowed)
                    : new GridGraph(grid, isDiagonalMovementAllowed);
        }
    }
}
