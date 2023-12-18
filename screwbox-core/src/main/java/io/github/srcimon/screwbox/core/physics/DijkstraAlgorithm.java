package io.github.srcimon.screwbox.core.physics;

import io.github.srcimon.screwbox.core.Grid;

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
    public List<Grid.Node> findPath(final Grid grid, final Grid.Node start, final Grid.Node end) {
        final var usedNodes = new ArrayList<Grid.Node>();
        usedNodes.add(start);

        while (true) {
            final List<Grid.Node> openNodes = calculateOpenNodes(grid, usedNodes);

            for (final Grid.Node point : openNodes) {
                usedNodes.add(point);
                if (end.equals(point)) {
                    final Grid.Node lastNode = usedNodes.getLast();
                    return grid.backtrack(lastNode);
                }
            }

            if (openNodes.isEmpty()) {
                return emptyList();
            }
        }
    }

    private List<Grid.Node> calculateOpenNodes(final Grid grid, final List<Grid.Node> usedNodes) {
        final List<Grid.Node> openNodes = new ArrayList<>();
        for (final var usedNode : usedNodes) {
            for (final Grid.Node neighbor : grid.reachableNeighbors(usedNode)) {
                if (!usedNodes.contains(neighbor) && !openNodes.contains(neighbor)) {
                    openNodes.add(neighbor);
                }
            }
        }
        return openNodes;
    }

}
