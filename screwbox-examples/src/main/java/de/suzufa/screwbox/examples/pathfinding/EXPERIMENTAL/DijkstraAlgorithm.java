package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.physics.Grid;
import de.suzufa.screwbox.core.physics.Grid.Node;

public class DijkstraAlgorithm implements PathfindingAlgorithm {

    @Override
    public List<Node> findPath(final Grid raster, final Node start, final Node end) {
        final List<Node> used = new ArrayList<>();
        used.add(start);

        while (true) {
            final List<Node> newOpen = new ArrayList<>();
            for (var use : used) {
                for (final Node neighbor : raster.findNeighbors(use)) {
                    if (!used.contains(neighbor) && !newOpen.contains(neighbor)) {
                        newOpen.add(neighbor);
                    }
                }
            }

            for (final Node point : newOpen) {
                used.add(point);
                if (end.equals(point)) {
                    return constructPath(used);
                }
            }

            if (newOpen.isEmpty()) {
                return emptyList();
            }
        }
    }

    private List<Node> constructPath(final List<Node> used) {
        final List<Node> path = new ArrayList<>();
        Node point = used.get(used.size() - 1);
        while (nonNull(point.parent())) {
            path.add(0, point);
            point = point.parent();
        }
        return path;
    }

}
