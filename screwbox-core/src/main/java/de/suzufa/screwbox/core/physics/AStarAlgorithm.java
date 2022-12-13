package de.suzufa.screwbox.core.physics;

import static java.util.Collections.emptyList;

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
        final Set<Node> visited = new HashSet<Node>();
        final Map<Node, Double> distances = new HashMap<>();
        final Queue<NodeWithCosts> nodesWithCosts = new PriorityQueue<>();

        distances.put(start, 0.0);
        nodesWithCosts.add(new NodeWithCosts(start, 0.0));
        NodeWithCosts current = null;

        while (!nodesWithCosts.isEmpty()) {
            current = nodesWithCosts.remove();

            if (!visited.contains(current.node)) {
                visited.add(current.node);
                if (current.node.equals(end)) {
                    return grid.backtrack(current.node);
                }
                for (final Node neighbor : grid.reachableNeighbors(current.node)) {
                    if (!visited.contains(neighbor)) {
                        final double predictedDistance = distance(neighbor, end);
                        final double neighborDistance = distance(current.node, neighbor);
                        final double totalDistance = distance(current.node, start) + neighborDistance
                                + predictedDistance;

                        final Double distanceNeighbour = distances.get(neighbor);
                        if (distanceNeighbour == null || totalDistance < distanceNeighbour) {
                            distances.put(neighbor, totalDistance);
                            nodesWithCosts.add(new NodeWithCosts(neighbor, totalDistance));
                        }
                    }
                }
            }
        }
        return emptyList();
    }

    private double distance(final Node neighbor, final Node endNode) {
        final int deltaX = neighbor.x() - endNode.x();
        final int deltaY = neighbor.y() - endNode.y();
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
