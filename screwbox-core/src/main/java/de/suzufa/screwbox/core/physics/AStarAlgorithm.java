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

    private record NodeWithCosts(Node node, int cost) implements Comparable<NodeWithCosts> {

        @Override
        public int compareTo(NodeWithCosts other) {
            return Integer.compare(cost, other.cost); // TODO: maybe falsch herum
        }

    }

    // TODO: see https://www.geeksforgeeks.org/a-search-algorithm/
    @Override
    public List<Node> findPath(Grid grid, Node start, Node end) {
        return aStarSearch(grid, start, end);
    }

    /**
     * Find the path from start to goal using A-Star search
     *
     * @param start The starting location
     * @param goal  The goal location
     * @return The list of intersections that form the shortest path from start to
     *         goal (including both start and goal).
     */
    public List<Node> aStarSearch(Grid grid, Node startNode, Node endNode) {

        HashSet<Node> visited = new HashSet<Node>();
        Map<Node, Integer> distances = new HashMap<>();
        for (Node node : grid.nodes()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        Queue<NodeWithCosts> priorityQueue = new PriorityQueue<>();

        distances.put(startNode, 0);
        priorityQueue.add(new NodeWithCosts(startNode, 0));
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
                        int predictedDistance = distance(neighbor, endNode);

                        // 1. calculate distance to neighbor. 2. calculate dist from start node
                        int neighborDistance = 2;
                        int totalDistance = distance(current.node, startNode) + neighborDistance + predictedDistance;

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

    private int distance(Node neighbor, Node endNode) {
        int xD = neighbor.x() - endNode.x();
        int yD = neighbor.y() - endNode.y();
        return xD * xD + yD * yD;
    }
}
