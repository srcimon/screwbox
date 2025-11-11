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
        searchData.costs.put(start, 0.0);
        searchData.open.add(new PathfindingNode<>(start, null));
        while (!searchData.open.isEmpty()) {
            final PathfindingNode<T> currentNode = searchData.open.remove();
            if (currentNode.node().equals(end)) {
                return currentNode.backtrack();
            }
            if(searchData.closed.add(currentNode.node())) {
                processNode(graph, start, end, searchData, currentNode);
            }
        }
        return emptyList();
    }

    private static <T> void processNode(final Graph<T> graph, final T start, final T end, final AStarData<T> data, final PathfindingNode<T> currentNode) {
        final var costToStart = data.costsToStart.get(currentNode.node());
        for (final var node : graph.adjacentNodes(currentNode.node())) {
            if (!data.closed.contains(node)) {
                final double startCost = isNull(costToStart) ? graph.traversalCost(currentNode.node(), start) : costToStart;
                final double totalCost = startCost + graph.traversalCost(node, currentNode.node()) + graph.traversalCost(node, end);
                final Double costNeighbour = data.costs.get(node);
                if (isNull(costNeighbour) || totalCost < costNeighbour) {
                    data.costsToStart.put(node, totalCost);
                    data.costs.put(node, totalCost);
                    data.open.add(new PathfindingNode<>(node, currentNode, totalCost));
                }
            }
        }
    }

}
