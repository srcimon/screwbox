package io.github.simonbas.screwbox.examples.gameoflife.components;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.Grid;
import io.github.simonbas.screwbox.core.Grid.Node;
import io.github.simonbas.screwbox.core.entities.Component;
import io.github.simonbas.screwbox.core.utils.ListUtil;

import java.util.List;

public class GridComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Grid grid;

    public GridComponent() {
        grid = new Grid(Bounds.$$(-480, -480, 960, 960), 2);
        List<Node> nodes = grid.nodes();
        for (int i = 0; i < grid.width() * 30; i++) {
            grid.block(ListUtil.randomFrom(nodes));
        }
    }
}