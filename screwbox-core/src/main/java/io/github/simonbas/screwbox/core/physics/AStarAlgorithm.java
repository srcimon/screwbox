package io.github.simonbas.screwbox.core.physics;

import io.github.simonbas.screwbox.core.Grid;
import io.github.simonbas.screwbox.core.Grid.Node;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

/**
 * An implementation of the A* algorithm.
 * <p>
 * See <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">Wikipedia</a>
 */
public class AStarAlgorithm implements PathfindingAlgorithm {

    @Override
    public List<Node> findPath(final Grid grid, final Node start, final Node end) {
        return new AStarSearch(grid, start, end).findPath();
    }

    private record WeightedNode(Node node, Double cost) implements Comparable<WeightedNode> {

        @Override
        public int compareTo(final WeightedNode other) {
            return Double.compare(cost, other.cost);
        }
    }

    private static class AStarSearch {

        private final Grid grid;
        private final Node start;
        private final Node end;
        private final Set<Node> closed;
        private final Map<Node, Double> costs;
        private final Map<Node, Double> costsToStart;
        private final Queue<WeightedNode> open;

        public AStarSearch(Grid grid, Node start, Node end) {
            this.grid = grid;
            this.start = start;
            this.end = end;
            closed = new HashSet<>();
            costs = new HashMap<>(Map.of(start, 0.0));
            costsToStart = new HashMap<>();
            open = new PriorityQueue<>(List.of(new WeightedNode(start, 0.0)));
        }

        public List<Node> findPath() {
            while (!open.isEmpty()) {
                final Node currentNode = open.remove().node;
                if (!closed.contains(currentNode)) {
                    closed.add(currentNode);
                    if (currentNode.equals(end)) {
                        return grid.backtrack(currentNode);
                    }
                    processNode(currentNode);
                }
            }
            return emptyList();
        }

        private void processNode(final Node currentNode) {
            final var costToStart = costsToStart.get(currentNode);
            for (final Node neighbor : grid.reachableNeighbors(currentNode)) {
                if (!closed.contains(neighbor)) {
                    final double startCost = isNull(costToStart) ? currentNode.distance(start) : costToStart;
                    final double totalCost = startCost + neighbor.distance(currentNode)
                            + neighbor.distance(end);
                    final Double costNeighbour = costs.get(neighbor);
                    if (isNull(costNeighbour) || totalCost < costNeighbour) {
                        costsToStart.put(neighbor, totalCost);
                        costs.put(neighbor, totalCost);
                        open.add(new WeightedNode(neighbor, totalCost));
                    }
                }
            }
        }
    }

}
