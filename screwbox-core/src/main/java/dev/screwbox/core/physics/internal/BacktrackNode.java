package dev.screwbox.core.physics.internal;

import dev.screwbox.core.physics.Grid;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Deprecated
public record BacktrackNode(Grid.Node node, BacktrackNode parent) {

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