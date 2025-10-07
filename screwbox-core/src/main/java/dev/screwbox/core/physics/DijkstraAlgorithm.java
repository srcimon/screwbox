package dev.screwbox.core.physics;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.physics.internal.NodePath;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * An implementation of the Dijkstra algorithm.
 * <p>
 * See <a href="https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm">Wikipedia</a>
 */
public class DijkstraAlgorithm implements PathfindingAlgorithm {

    @Override
    public List<Offset> findPath(final Grid grid, final Offset start, final Offset end) {
        final var usedNodes = new ArrayList<NodePath>();
        usedNodes.add(new NodePath(start, null));

        while (true) {
            final List<NodePath> openNodes = calculateOpenNodes(grid, usedNodes);

            for (final NodePath point : openNodes) {
                usedNodes.add(point);
                if (end.equals(point.node())) {
                    return usedNodes.getLast().backtrack();
                }
            }

            if (openNodes.isEmpty()) {
                return emptyList();
            }
        }

    }

    private List<NodePath> calculateOpenNodes(final Grid grid, final List<NodePath> usedNodes) {
        final List<NodePath> openNodes = new ArrayList<>();
        for (final var usedNode : usedNodes) {
            for (final Offset neighbor : grid.reachableNeighbors(usedNode.node())) {
                if (usedNodes.stream().noneMatch(n -> n.node().equals(neighbor)) && openNodes.stream().noneMatch(n -> n.node().equals(neighbor))) {
                    openNodes.add(new NodePath(neighbor, usedNode));
                }
            }
        }
        return openNodes;
    }

}
