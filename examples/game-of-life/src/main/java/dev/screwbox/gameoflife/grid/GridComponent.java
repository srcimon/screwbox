package dev.screwbox.gameoflife.grid;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.navigation.Grid;

import java.io.Serial;
import java.util.Random;

import static dev.screwbox.core.Bounds.$$;

public class GridComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Grid grid;

    public Color noNeighboursColor = Color.RED;
    public Color oneNeighboursColor = Color.BLUE;
    public Color twoNeighboursColor = Color.WHITE;

    public GridComponent() {
        final Random random = new Random();
        grid = new Grid($$(-1500, -1500, 1500, 1500), 2);
        for (int i = 0; i < grid.width() * 60; i++) {
            grid.block(random.nextInt(0, grid.width()), random.nextInt(0, grid.height()));
        }
    }
}