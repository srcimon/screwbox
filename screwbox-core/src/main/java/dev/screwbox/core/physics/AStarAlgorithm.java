package dev.screwbox.core.physics;

import dev.screwbox.core.physics.internal.NodePath;

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
public class AStarAlgorithm implements PathfindingAlgorithm {

    @Override
    public List<Grid.Node> findPath(final Grid grid, final Grid.Node start, final Grid.Node end) {
        return new AStarSearch(grid, start, end).findPath();
    }

    private record WeightedNode(NodePath node, Double cost) implements Comparable<WeightedNode> {

        @Override
        public int compareTo(final WeightedNode other) {
            return Double.compare(cost, other.cost);
        }
    }

    private static class AStarSearch {

        private final Grid grid;
        private final Grid.Node start;
        private final Grid.Node end;
        private final Set<NodePath> closed;
        private final Map<Grid.Node, Double> costs;
        private final Map<Grid.Node, Double> costsToStart;
        private final Queue<WeightedNode> open;

        public AStarSearch(Grid grid, Grid.Node start, Grid.Node end) {
            this.grid = grid;
            this.start = start;
            this.end = end;
            closed = new HashSet<>();
            costs = new HashMap<>(Map.of(start, 0.0));
            costsToStart = new HashMap<>();
            open = new PriorityQueue<>(List.of(new WeightedNode(new NodePath(start, null), 0.0)));
        }

        public List<Grid.Node> findPath() {
            while (!open.isEmpty()) {
                final NodePath currentNode = open.remove().node;
                if (!closed.contains(currentNode)) {
                    closed.add(currentNode);
                    if (currentNode.node().equals(end)) {
                        return currentNode.backtrack();
                    }
                    processNode(currentNode);
                }
            }
            return emptyList();
        }

        private void processNode(final NodePath currentNode) {
            final var costToStart = costsToStart.get(currentNode);
            for (final var neighbor : grid.reachableNeighbors(currentNode.node())) {
                if (!closed.contains(neighbor)) {
                    final double startCost = isNull(costToStart) ? currentNode.node().distance(start) : costToStart;
                    final double totalCost = startCost + neighbor.distance(currentNode.node())
                            + neighbor.distance(end);
                    final Double costNeighbour = costs.get(neighbor);
                    if (isNull(costNeighbour) || totalCost < costNeighbour) {
                        costsToStart.put(neighbor, totalCost);
                        costs.put(neighbor, totalCost);
                        open.add(new WeightedNode(new NodePath(neighbor, currentNode), totalCost));
                    }
                }
            }
        }
    }

}
