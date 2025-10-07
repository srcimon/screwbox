package dev.screwbox.core.physics;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;

/**
 * An implementation of the Dijkstra algorithm.
 * <p>
 * See <a href="https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm">Wikipedia</a>
 */
public class DijkstraAlgorithm implements PathfindingAlgorithm {

    record BacktrackNode(Grid.Node node, BacktrackNode parent) {

        public List<Grid.Node> backtrack() {
            List<Grid.Node> backtrackList = new ArrayList<>();
            backtrack(backtrackList, this);

            return backtrackList.reversed();
        }

        private void backtrack(final List<Grid.Node> nodes, final BacktrackNode parent) {
            if (nonNull(parent) && nonNull(parent.parent)) {
                nodes.add(parent.node);
                backtrack(nodes, parent.parent);
            }
        }
    }

    @Override
    public List<Grid.Node> findPath(final Grid grid, final Grid.Node start, final Grid.Node end) {
        final var usedNodes = new ArrayList<BacktrackNode>();
        usedNodes.add(new BacktrackNode(start, null));

        while (true) {
            final List<BacktrackNode> openNodes = calculateOpenNodes(grid, usedNodes);

            for (final BacktrackNode point : openNodes) {
                usedNodes.add(point);
                if (end.equals(point.node)) {
                    final var lastNode = usedNodes.getLast();
                    List<Grid.Node> backtrack = lastNode.backtrack();
                    return backtrack;
                }
            }

            if (openNodes.isEmpty()) {
                return emptyList();
            }
        }

    }

    private List<BacktrackNode> calculateOpenNodes(final Grid grid, final List<BacktrackNode> usedNodes) {
        final List<BacktrackNode> openNodes = new ArrayList<>();
        for (final var usedNode : usedNodes) {
            for (final Grid.Node neighbor : grid.reachableNeighbors(usedNode.node)) {
                if (usedNodes.stream().noneMatch(n -> n.node.equals(neighbor)) && !openNodes.contains(neighbor)) {
                    openNodes.add(new BacktrackNode(neighbor, usedNode));
                }
            }
        }
        return openNodes;
    }

}
