package de.suzufa.screwbox.core.physics;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.List;

import de.suzufa.screwbox.core.Grid;
import de.suzufa.screwbox.core.Grid.Node;

/**
 * An implementation of the Dijkstra algorithm.
 * 
 * @see https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
 */
public class DijkstraAlgorithm implements PathfindingAlgorithm {

    @Override
    public List<Node> findPath(final Grid grid, final Node start, final Node end) {
        final var usedNodes = new ArrayList<Node>();
        usedNodes.add(start);

        while (true) {
            final List<Node> openNodes = calculateOpenNodes(grid, usedNodes);

            for (final Node point : openNodes) {
                usedNodes.add(point);
                if (end.equals(point)) {
                    final Node lastNode = usedNodes.get(usedNodes.size() - 1);
                    return grid.backtrack(lastNode);
                }
            }

            if (openNodes.isEmpty()) {
                return emptyList();
            }
        }
    }

    private List<Node> calculateOpenNodes(final Grid grid, final List<Node> usedNodes) {
        final List<Node> openNodes = new ArrayList<>();
        for (final var usedNode : usedNodes) {
            for (final Node neighbor : grid.reachableNeighbors(usedNode)) {
                if (!usedNodes.contains(neighbor) && !openNodes.contains(neighbor)) {
                    openNodes.add(neighbor);
                }
            }
        }
        return openNodes;
    }

}
