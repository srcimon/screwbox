package de.suzufa.screwbox.core.physics.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Path;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.physics.DijkstraAlgorithm;
import de.suzufa.screwbox.core.physics.Grid;
import de.suzufa.screwbox.core.physics.Grid.Node;
import de.suzufa.screwbox.core.physics.PathfindingAlgorithm;
import de.suzufa.screwbox.core.physics.Physics;
import de.suzufa.screwbox.core.physics.RaycastBuilder;
import de.suzufa.screwbox.core.physics.SelectEntityBuilder;

public class DefaultPhysics implements Physics {

    private final Engine engine;

    private PathfindingAlgorithm pathfindingAlgorithm = new DijkstraAlgorithm();

    public DefaultPhysics(final Engine engine) {
        this.engine = engine;
    }

    @Override
    public RaycastBuilder raycastFrom(final Vector position) {
        return new RaycastBuilder(engine.entityEngine(), position);
    }

    @Override
    public SelectEntityBuilder searchAtPosition(final Vector position) {
        return new SelectEntityBuilder(engine.entityEngine(), position);
    }

    @Override
    public SelectEntityBuilder searchInRange(final Bounds range) {
        return new SelectEntityBuilder(engine.entityEngine(), range);
    }

    @Override
    public Optional<Path> findPath(Grid grid, Vector start, Vector end) {
        Node startPoint = grid.toGrid(start);
        Node endPoint = grid.toGrid(end);

        List<Node> path = pathfindingAlgorithm.findPath(grid, startPoint, endPoint);
        if (path.isEmpty()) {
            return Optional.empty();
        }

        List<Vector> list = new ArrayList<>();
        list.add(start);
        boolean first = true;
        for (var node : path) {
            if (!first) {
                list.add(grid.toWorld(node));
            }
            first = false;
        }
        list.remove(list.size() - 1);
        list.add(end);
        return Optional.of(Path.withNodes(list));
    }

    @Override
    public Physics setPathfindingAlgorithm(PathfindingAlgorithm algorithm) {
        this.pathfindingAlgorithm = algorithm;
        return this;
    }
}
