package dev.screwbox.core.navigation;

import dev.screwbox.core.navigation.internal.ChainedNode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

/**
 * An implementation of the A* algorithm. Considers cost of traversal.
 *
 * @see <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">Wikipedia</a>
 */
public class AStarAlgorithm<T> implements PathfindingAlgorithm<T> {

    @Override
    public List<T> findPath(final Graph<T> graph, final T start, final T end) {
        final Set<T> closed = new HashSet<>();
        final Map<T, Double> costs = new HashMap<>(Map.of(start, 0.0));
        final Map<T, Double> costsToStart = new HashMap<>();
        final Queue<ChainedNode<T>> open = new PriorityQueue<>(List.of(new ChainedNode<>(start, null)));
        while (!open.isEmpty()) {
            final ChainedNode<T> currentNode = open.remove();
            if (!closed.contains(currentNode.node())) {
                closed.add(currentNode.node());
                if (currentNode.node().equals(end)) {
                    return currentNode.backtrack();
                }
                final var costToStart = costsToStart.get(currentNode.node());
                for (final var neighbor : graph.adjacentNodes(currentNode.node())) {
                    if (!closed.contains(neighbor)) {
                        final double startCost = isNull(costToStart) ? graph.traversalCost(currentNode.node(), start) : costToStart;
                        final double totalCost = startCost + graph.traversalCost(neighbor, currentNode.node())
                                                 + graph.traversalCost(neighbor, end);
                        final Double costNeighbour = costs.get(neighbor);
                        if (isNull(costNeighbour) || totalCost < costNeighbour) {
                            costsToStart.put(neighbor, totalCost);
                            costs.put(neighbor, totalCost);
                            open.add(new ChainedNode<>(neighbor, currentNode, totalCost));
                        }
                    }
                }
            }
        }
        return emptyList();
    }

}
