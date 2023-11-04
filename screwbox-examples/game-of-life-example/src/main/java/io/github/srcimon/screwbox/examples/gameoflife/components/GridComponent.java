package io.github.srcimon.screwbox.examples.gameoflife.components;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Grid;
import io.github.srcimon.screwbox.core.Grid.Node;
import io.github.srcimon.screwbox.core.entities.Component;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.utils.ListUtil;

import java.util.List;

public class GridComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Grid grid;

    public Color noNeighboursColor = Color.RED;
    public Color oneNeighboursColor = Color.BLUE;
    public Color twoNeighboursColor = Color.WHITE;

    public GridComponent() {
        grid = new Grid(Bounds.$$(-480, -480, 960, 960), 2);
        List<Node> nodes = grid.nodes();
        for (int i = 0; i < grid.width() * 30; i++) {
            grid.block(ListUtil.randomFrom(nodes));
        }
    }
}