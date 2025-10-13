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
 * An implementation of the A* algorithm.
 *
 * @see <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">Wikipedia</a>
 */
public class AStarAlgorithm<T> implements PathfindingAlgorithm<T> {

    @Override
    public List<T> findPath(final NodeGraph<T> nodeGraph, final T start, final T end) {
        return new AStarSearch<>(nodeGraph, start, end).findPath();
    }

    private static class AStarSearch<T> {

        private final NodeGraph<T> nodeGraph;
        private final T start;
        private final T end;
        private final Set<T> closed;
        private final Map<T, Double> costs;
        private final Map<T, Double> costsToStart;
        private final Queue<ChainedNode<T>> open;

        public AStarSearch(final NodeGraph<T> nodeGraph, final T start, final T end) {
            this.nodeGraph = nodeGraph;
            this.start = start;
            this.end = end;
            closed = new HashSet<>();
            costs = new HashMap<>(Map.of(start, 0.0));
            costsToStart = new HashMap<>();
            open = new PriorityQueue<>(List.of(new ChainedNode<>(start, null)));
        }

        public List<T> findPath() {
            while (!open.isEmpty()) {
                final ChainedNode<T> currentNode = open.remove();
                if (!closed.contains(currentNode.node())) {
                    closed.add(currentNode.node());
                    if (currentNode.node().equals(end)) {
                        return currentNode.backtrack();
                    }
                    processNode(currentNode);
                }
            }
            return emptyList();
        }

        private void processNode(final ChainedNode<T> currentNode) {
            final var costToStart = costsToStart.get(currentNode.node());
            for (final var neighbor : nodeGraph.adjacentNodes(currentNode.node())) {
                if (!closed.contains(neighbor)) {
                    final double startCost = isNull(costToStart) ? nodeGraph.traversalCost(currentNode.node(), start) : costToStart;
                    final double totalCost = startCost + nodeGraph.traversalCost(neighbor, currentNode.node())
                                             + nodeGraph.traversalCost(neighbor, end);
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

}
