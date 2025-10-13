package dev.screwbox.core.navigation.internal;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public record ChainedNode<T>(T node, ChainedNode<T> parent, double cost) implements Comparable<ChainedNode<T>> {

    public ChainedNode(T node) {
        this(node, null);
    }

    public ChainedNode(T node, ChainedNode<T> parent) {
        this(node, parent, 0);
    }

    public List<T> backtrack() {
        final var backtrackList = new ArrayList<T>();
        backtrack(backtrackList, this);
        return backtrackList.reversed();
    }

    public T node() {
        return node;
    }

    private void backtrack(final List<T> nodes, final ChainedNode<T> parent) {
        if (nonNull(parent) && nonNull(parent.parent)) {
            nodes.add(parent.node);
            backtrack(nodes, parent.parent);
        }
    }

    @Override
    public int compareTo(final ChainedNode<T> other) {
        return Double.compare(cost, other.cost);
    }
}