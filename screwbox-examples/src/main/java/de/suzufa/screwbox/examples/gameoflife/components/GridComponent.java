package de.suzufa.screwbox.examples.gameoflife.components;

import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Grid;
import de.suzufa.screwbox.core.Grid.Node;
import de.suzufa.screwbox.core.entities.Component;
import de.suzufa.screwbox.core.utils.ListUtil;

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