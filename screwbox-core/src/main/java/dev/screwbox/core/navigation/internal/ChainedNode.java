package dev.screwbox.core.navigation.internal;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public record ChainedNode<T>(T node, ChainedNode<T> parent) {

    public List<T> backtrack() {
        final var backtrackList = new ArrayList<T>();
        backtrack(backtrackList, this);
        return backtrackList.reversed();
    }

    private void backtrack(final List<T> nodes, final ChainedNode<T> parent) {
        if (nonNull(parent) && nonNull(parent.parent)) {
            nodes.add(parent.node);
            backtrack(nodes, parent.parent);
        }
    }
}