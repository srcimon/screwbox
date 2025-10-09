package dev.screwbox.core.physics;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.physics.internal.ChainedOffset;

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
        final var usedNodes = new ArrayList<ChainedOffset>();
        usedNodes.add(new ChainedOffset(start, null));

        while (true) {
            final List<ChainedOffset> openNodes = calculateOpenNodes(grid, usedNodes);

            for (final ChainedOffset point : openNodes) {
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

    private List<ChainedOffset> calculateOpenNodes(final Grid grid, final List<ChainedOffset> usedNodes) {
        final List<ChainedOffset> openNodes = new ArrayList<>();
        for (final var usedNode : usedNodes) {
            for (final Offset neighbor : grid.reachableNeighbors(usedNode.node())) {
                if (usedNodes.stream().noneMatch(n -> n.node().equals(neighbor)) && openNodes.stream().noneMatch(n -> n.node().equals(neighbor))) {
                    openNodes.add(new ChainedOffset(neighbor, usedNode));
                }
            }
        }
        return openNodes;
    }

}
