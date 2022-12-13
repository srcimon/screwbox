package de.suzufa.screwbox.core.physics;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import de.suzufa.screwbox.core.Grid;
import de.suzufa.screwbox.core.Grid.Node;

public class AStarAlgorithm implements PathfindingAlgorithm {

    private record NodeWithCosts(Node node, Double cost) implements Comparable<NodeWithCosts> {

        @Override
        public int compareTo(NodeWithCosts other) {
            return Double.compare(cost, other.cost);
        }
    }

    // TODO: see https://www.geeksforgeeks.org/a-search-algorithm/
    @Override
    public List<Node> findPath(Grid grid, Node start, Node end) {
        return aStarSearch(grid, start, end);
    }

    public List<Node> aStarSearch(Grid grid, Node startNode, Node endNode) {

        HashSet<Node> visited = new HashSet<Node>();
        Map<Node, Double> distances = new HashMap<>();
        for (Node node : grid.nodes()) {
            distances.put(node, Double.MAX_VALUE);
        }
        Queue<NodeWithCosts> priorityQueue = new PriorityQueue<>();

        distances.put(startNode, 0.0);
        priorityQueue.add(new NodeWithCosts(startNode, 0.0));
        NodeWithCosts current = null;

        while (!priorityQueue.isEmpty()) {
            current = priorityQueue.remove();

            if (!visited.contains(current.node)) {
                visited.add(current.node);
                // if last element in PQ reached
                if (current.node.equals(endNode)) {
                    return grid.backtrack(current.node);
                }
                for (Node neighbor : grid.reachableNeighbors(current.node)) {
                    if (!visited.contains(neighbor)) {

                        // calculate predicted distance to the end node
                        double predictedDistance = distance(neighbor, endNode);

                        // 1. calculate distance to neighbor. 2. calculate dist from start node
                        double neighborDistance = distance(current.node, neighbor);
                        double totalDistance = distance(current.node, startNode) + neighborDistance + predictedDistance;

                        // check if distance smaller
                        if (totalDistance < distances.get(neighbor)) {
                            // update n's distance
                            distances.put(neighbor, totalDistance);
                            priorityQueue.add(new NodeWithCosts(neighbor, totalDistance));
                        }
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private double distance(Node neighbor, Node endNode) {
        int xD = neighbor.x() - endNode.x();
        int yD = neighbor.y() - endNode.y();
        return Math.sqrt(xD * xD + yD * yD);
    }
}
