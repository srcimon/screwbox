package dev.screwbox.core.physics.internal;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.physics.Grid;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Deprecated
public record NodePath(Offset node, NodePath parent) {

    public List<Offset> backtrack() {
        List<Offset> backtrackList = new ArrayList<>();
        backtrack(backtrackList, this);
        return backtrackList.reversed();
    }

    private void backtrack(final List<Offset> nodes, final NodePath parent) {
        if (nonNull(parent) && nonNull(parent.parent)) {
            nodes.add(parent.node);
            backtrack(nodes, parent.parent);
        }
    }
}