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

    private record AStarData<T>(Queue<PathfindingNode<T>> open, Set<T> closed, Map<T, Double> costs,
                                Map<T, Double> costsToStart) {

        private AStarData() {
            this(new PriorityQueue<>(), new HashSet<>(), new HashMap<>(), new HashMap<>());
        }
    }

    @Override
    public List<T> findPath(final Graph<T> graph, final T start, final T end) {
        final var searchData = new AStarData<T>();
        searchData.costsToStart.put(start, 0.0);
        final double initialFCost = graph.traversalCost(start, end);
        searchData.costs.put(start, initialFCost);
        searchData.open.add(new PathfindingNode<>(start, null, initialFCost));

        while (!searchData.open.isEmpty()) {
            final PathfindingNode<T> currentNode = searchData.open.remove();

            if (currentNode.node().equals(end)) {
                return currentNode.backtrack();
            }

            if (currentNode.cost() > searchData.costs.getOrDefault(currentNode.node(), Double.POSITIVE_INFINITY)) {
                continue;
            }

            if (searchData.closed.add(currentNode.node())) {
                processNode(graph, end, searchData, currentNode);
            }
        }
        return emptyList();
    }

    private static <T> void processNode(final Graph<T> graph, final T end, final AStarData<T> data, final PathfindingNode<T> currentNode) {
        final double costToStart = data.costsToStart.get(currentNode.node());

        for (final var neighbor : graph.adjacentNodes(currentNode.node())) {
            if (!data.closed.contains(neighbor)) {
                final double totalCost = costToStart + graph.traversalCost(currentNode.node(), neighbor);
                final Double costNeighbour = data.costsToStart.get(neighbor);

                if (isNull(costNeighbour) || totalCost < costNeighbour) {
                    final double fCost = totalCost + graph.traversalCost(neighbor, end);

                    data.costsToStart.put(neighbor, totalCost);
                    data.costs.put(neighbor, fCost);
                    data.open.add(new PathfindingNode<>(neighbor, currentNode, fCost));
                }
            }
        }
    }

}
