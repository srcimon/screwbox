package dev.screwbox.core.navigation;

import dev.screwbox.core.navigation.internal.ChainedNode;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

/**
 * An implementation of the Dijkstra algorithm.
 * <p>
 * See <a href="https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm">Wikipedia</a>
 */
public class DijkstraAlgorithm<T> implements PathfindingAlgorithm<T> {

    @Override
    public List<T> findPath(final NodeGraph<T> nodeGraph, final T start, final T end) {
        final var usedNodes = new ArrayList<ChainedNode<T>>();
        usedNodes.add(new ChainedNode<>(start, null));

        while (true) {
            final List<ChainedNode<T>> openNodes = calculateOpenNodes(nodeGraph, usedNodes);

            for (final ChainedNode<T> point : openNodes) {
                usedNodes.add(point);
                if (end.equals(point.node())) {
                    return usedNodes.getLast().backtrack();
                }
            }

            if (openNodes.isEmpty()) {
                return emptyList();
            }
        }

    }

    private List<ChainedNode<T>> calculateOpenNodes(final NodeGraph<T> nodeGraph, final List<ChainedNode<T>> usedNodes) {
        final List<ChainedNode<T>> openNodes = new ArrayList<>();
        for (final var usedNode : usedNodes) {
            for (final T neighbor : nodeGraph.adjacentNodes(usedNode.node())) {
                if (usedNodes.stream().noneMatch(n -> n.node().equals(neighbor)) && openNodes.stream().noneMatch(n -> n.node().equals(neighbor))) {
                    openNodes.add(new ChainedNode<>(neighbor, usedNode));
                }
            }
        }
        return openNodes;
    }

}
