package de.suzufa.screwbox.core.physics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.suzufa.screwbox.core.Grid;
import de.suzufa.screwbox.core.Grid.Node;

public class AStarAlgorithm implements PathfindingAlgorithm {

    private record NodeWithCosts(Node node, int cost, NodeWithCosts parent) implements Comparable<NodeWithCosts> {

        @Override
        public int compareTo(NodeWithCosts other) {
            return Integer.compare(other.cost, cost); // TODO: maybe falsch herum
        }

    }

    // TODO: see https://www.geeksforgeeks.org/a-search-algorithm/
    @Override
    public List<Node> findPath(Grid grid, Node start, Node end) {
        List<NodeWithCosts> openList = new ArrayList<>();
        List<NodeWithCosts> closedList = new ArrayList<>();
        openList.add(new NodeWithCosts(start, 0, null));

        while (!openList.isEmpty()) {
            NodeWithCosts next = nodeWithLowestCost(openList);
            openList.remove(next);
            for (Node neighbor : grid.neighbors(next.node())) {
                if (neighbor.equals(end)) {
                    // TODO: STOP;
                } else {
                    int cost = neighbor
                    NodeWithCosts neighborNode = new NodeWithCosts(neighbor, 0, next);
                }
            }
        }
        return null;
    }

    private int distance()

    private NodeWithCosts nodeWithLowestCost(List<NodeWithCosts> openList) {
        Collections.sort(openList);
        return openList.get(0);
    }

}
