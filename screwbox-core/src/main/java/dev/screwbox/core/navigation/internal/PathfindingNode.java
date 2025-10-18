package dev.screwbox.core.navigation.internal;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public record PathfindingNode<T>(T node, PathfindingNode<T> parent, double cost)
        implements Comparable<PathfindingNode<T>> {

    public PathfindingNode(T node) {
        this(node, null);
    }

    public PathfindingNode(T node, PathfindingNode<T> parent) {
        this(node, parent, 0);
    }

    public List<T> backtrack() {
        final var backtrackList = new ArrayList<T>();
        backtrack(backtrackList, this);
        return backtrackList.reversed();
    }

    private void backtrack(final List<T> nodes, final PathfindingNode<T> parent) {
        if (nonNull(parent) && nonNull(parent.parent)) {
            nodes.add(parent.node);
            backtrack(nodes, parent.parent);
        }
    }

    @Override
    public int compareTo(final PathfindingNode<T> other) {
        return Double.compare(cost, other.cost);
    }
}