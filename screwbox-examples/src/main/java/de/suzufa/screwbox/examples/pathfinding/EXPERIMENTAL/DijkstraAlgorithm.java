package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.physics.Grid;
import de.suzufa.screwbox.core.physics.GridNode;

public class DijkstraAlgorithm implements PathfindingAlgorithm {

    @Override
    public List<GridNode> findPath(final Grid raster, final GridNode start, final GridNode end) {
        final List<GridNode> used = new ArrayList<>();
        used.add(start);

        while (true) {
            final List<GridNode> newOpen = new ArrayList<>();
            for (var use : used) {
                for (final GridNode neighbor : raster.findNeighbors(use)) {
                    if (!used.contains(neighbor) && !newOpen.contains(neighbor)) {
                        newOpen.add(neighbor);
                    }
                }
            }

            for (final GridNode point : newOpen) {
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

    private List<GridNode> constructPath(final List<GridNode> used) {
        final List<GridNode> path = new ArrayList<>();
        GridNode point = used.get(used.size() - 1);
        while (nonNull(point.parent())) {
            path.add(0, point);
            point = point.parent();
        }
        return path;
    }

}
