package dev.screwbox.core.navigation;

import dev.screwbox.core.navigation.internal.PathfindingNode;

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
 * An implementation of the A* algorithm. Optimizes route by considering
 * {@link Graph#traversalCost(Object, Object) cost of traversal}.
 * <p>
 * This is the recommended algorithm for finding paths in ScrewBox.
 *
 * @see <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">Wikipedia</a>
 */
public class AStarAlgorithm<T> implements PathfindingAlgorithm<T> {

    @Override
    public List<T> findPath(final Graph<T> graph, final T start, final T end) {
        final Set<T> closed = new HashSet<>();
        final Map<T, Double> costs = new HashMap<>(Map.of(start, 0.0));
        final Map<T, Double> costsToStart = new HashMap<>();
        final Queue<PathfindingNode<T>> open = new PriorityQueue<>(List.of(new PathfindingNode<>(start, null)));
        while (!open.isEmpty()) {
            final PathfindingNode<T> currentNode = open.remove();
            if (closed.contains(currentNode.node())) {
                continue;
            }
            closed.add(currentNode.node());
            if (currentNode.node().equals(end)) {
                return currentNode.backtrack();
            }
            final var costToStart = costsToStart.get(currentNode.node());
            for (final var node : graph.adjacentNodes(currentNode.node())) {
                if (!closed.contains(node)) {
                    final double totalCost = calculateCost(graph, start, end, node, costToStart, currentNode);
                    final Double costNeighbour = costs.get(node);
                    if (isNull(costNeighbour) || totalCost < costNeighbour) {
                        costsToStart.put(node, totalCost);
                        costs.put(node, totalCost);
                        open.add(new PathfindingNode<>(node, currentNode, totalCost));
                    }
                }
            }
        }
        return emptyList();
    }

    private static <T> double calculateCost(final Graph<T> graph, final T start, final T end, final T node, final Double costToStart, final PathfindingNode<T> currentNode) {
        final double startCost = isNull(costToStart) ? graph.traversalCost(currentNode.node(), start) : costToStart;
        return startCost + graph.traversalCost(node, currentNode.node()) + graph.traversalCost(node, end);
    }

}
