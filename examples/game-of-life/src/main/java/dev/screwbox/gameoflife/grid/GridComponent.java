package dev.screwbox.gameoflife.grid;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.navigation.Grid;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.utils.ListUtil;

import java.io.Serial;
import java.util.List;

import static dev.screwbox.core.Bounds.$$;

public class GridComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Grid grid;

    public Color noNeighboursColor = Color.RED;
    public Color oneNeighboursColor = Color.BLUE;
    public Color twoNeighboursColor = Color.WHITE;

    public GridComponent() {
        grid = new Grid($$(-2000, -2000, 2000, 2000), 2);
        final List<Offset> nodes = grid.nodes();
        for (int i = 0; i < grid.width() * 300; i++) {
            grid.block(ListUtil.randomFrom(nodes));
        }
    }
}