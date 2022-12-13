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

public class AStarAlgorithm implements PathfindingAlgorithm {

    private record NodeWithCosts(Node node, Double cost) implements Comparable<NodeWithCosts> {

        @Override
        public int compareTo(final NodeWithCosts other) {
            return Double.compare(cost, other.cost);
        }
    }

    @Override
    public List<Node> findPath(final Grid grid, final Node start, final Node end) {
        final Set<Node> closed = new HashSet<Node>();
        final Map<Node, Double> costs = new HashMap<>();
        final Map<Node, Double> costToStart = new HashMap<>();
        final Queue<NodeWithCosts> open = new PriorityQueue<>();

        costs.put(start, 0.0);
        open.add(new NodeWithCosts(start, 0.0));
        while (!open.isEmpty()) {
            NodeWithCosts current = open.remove();

            if (!closed.contains(current.node)) {
                closed.add(current.node);
                if (current.node.equals(end)) {
                    return grid.backtrack(current.node);
                }
                for (final Node neighbor : grid.reachableNeighbors(current.node)) {
                    if (!closed.contains(neighbor)) {
                        var cts = costToStart.get(current.node);
                        final double cost = cts == null ? current.node.distance(start)
                                : cts
                                        + neighbor.distance(current.node)
                                        + neighbor.distance(end);

                        final Double costNeighbour = costs.get(neighbor);
                        if (isNull(costNeighbour) || cost < costNeighbour) {
                            costToStart.put(neighbor, cost);
                            costs.put(neighbor, cost);
                            open.add(new NodeWithCosts(neighbor, cost));
                        }
                    }
                }
            }
        }
        return emptyList();
    }

}
