package dev.screwbox.core.navigation;

import dev.screwbox.core.navigation.internal.PathfindingNode;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * An implementation of the Dijkstra algorithm. Finds the path with the smallest amount of hops but does not consider
 * {@link Graph#traversalCost(Object, Object) cost of traversal}. In most cases using the {@link AStarAlgorithm} is
 * the faster and better way of finding a path.
 * <p>
 * See <a href="https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm">Wikipedia</a>
 */
public class DijkstraAlgorithm<T> implements PathfindingAlgorithm<T> {

    @Override
    public List<T> findPath(final Graph<T> graph, final T start, final T end) {
        final var usedNodes = new ArrayList<PathfindingNode<T>>();
        usedNodes.add(new PathfindingNode<>(start));

        while (true) {
            final List<PathfindingNode<T>> openNodes = calculateOpenNodes(graph, usedNodes);

            for (final PathfindingNode<T> point : openNodes) {
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

    private List<PathfindingNode<T>> calculateOpenNodes(final Graph<T> graph, final List<PathfindingNode<T>> usedNodes) {
        final List<PathfindingNode<T>> openNodes = new ArrayList<>();
        for (final var usedNode : usedNodes) {
            for (final T neighbor : graph.adjacentNodes(usedNode.node())) {
                if (usedNodes.stream().noneMatch(n -> n.node().equals(neighbor)) && openNodes.stream().noneMatch(n -> n.node().equals(neighbor))) {
                    openNodes.add(new PathfindingNode<>(neighbor, usedNode));
                }
            }
        }
        return openNodes;
    }

}
