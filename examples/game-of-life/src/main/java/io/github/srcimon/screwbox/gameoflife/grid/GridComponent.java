package io.github.srcimon.screwbox.gameoflife.grid;

import dev.screwbox.core.Grid;
import dev.screwbox.core.Grid.Node;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.ListUtil;

import java.util.List;

import static dev.screwbox.core.Bounds.$$;

public class GridComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Grid grid;

    public Color noNeighboursColor = Color.RED;
    public Color oneNeighboursColor = Color.BLUE;
    public Color twoNeighboursColor = Color.WHITE;

    public GridComponent() {
        grid = new Grid($$(-480, -480, 960, 960), 2);
        List<Node> nodes = grid.nodes();
        for (int i = 0; i < grid.width() * 30; i++) {
            grid.block(ListUtil.randomFrom(nodes));
        }
    }
}