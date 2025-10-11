package dev.screwbox.core.navigation.internal;

import dev.screwbox.core.graphics.Offset;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

public record ChainedOffset(Offset node, ChainedOffset parent) {

    public List<Offset> backtrack() {
        final var backtrackList = new ArrayList<Offset>();
        backtrack(backtrackList, this);
        return backtrackList.reversed();
    }

    private void backtrack(final List<Offset> nodes, final ChainedOffset parent) {
        if (nonNull(parent) && nonNull(parent.parent)) {
            nodes.add(parent.node);
            backtrack(nodes, parent.parent);
        }
    }
}