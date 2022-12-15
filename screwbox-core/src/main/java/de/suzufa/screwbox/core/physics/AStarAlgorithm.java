package de.suzufa.screwbox.core.physics;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import de.suzufa.screwbox.core.Grid;
import de.suzufa.screwbox.core.Grid.Node;

/**
 * An implementation of the A* algorithm.
 * 
 * @see https://en.wikipedia.org/wiki/A*_search_algorithm
 */
public class AStarAlgorithm implements PathfindingAlgorithm {

    private record WeightedNode(Node node, Double cost) implements Comparable<WeightedNode> {

        @Override
        public int compareTo(final WeightedNode other) {
            return Double.compare(cost, other.cost);
        }
    }

    @Override
    public List<Node> findPath(final Grid grid, final Node start, final Node end) {
        final Set<Node> closed = new HashSet<>();
        final Map<Node, Double> costs = new HashMap<>(Map.of(start, 0.0));
        final Map<Node, Double> costsToStart = new HashMap<>();
        final Queue<WeightedNode> open = new PriorityQueue<>(List.of(new WeightedNode(start, 0.0)));

        while (!open.isEmpty()) {
            final WeightedNode current = open.remove();
            if (!closed.contains(current.node)) {
                closed.add(current.node);
                if (current.node.equals(end)) {
                    return grid.backtrack(current.node);
                }
                final var costToStart = costsToStart.get(current.node);
                for (final Node neighbor : grid.reachableNeighbors(current.node)) {
                    if (!closed.contains(neighbor)) {
                        final double startCost = isNull(costToStart) ? current.node.distance(start) : costToStart;
                        final double totalCost = startCost + neighbor.distance(current.node) + neighbor.distance(end);
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
        return emptyList();
    }

}
