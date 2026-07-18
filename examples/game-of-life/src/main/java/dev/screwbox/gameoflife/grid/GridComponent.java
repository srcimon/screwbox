package dev.screwbox.gameoflife.grid;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.navigation.BinaryGrid;

import java.io.Serial;
import java.util.Random;

import static dev.screwbox.core.Bounds.$$;

public class GridComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public BinaryGrid grid;

    public GridComponent() {
        final Random random = new Random();
        grid = new BinaryGrid($$(-1000, -1000, 1000, 1000), 2);
        for (int i = 0; i < grid.width() * 60; i++) {
            final int x = random.nextInt(0, grid.width());
            final int y = random.nextInt(0, grid.height());
            grid.set(x, y, true);
        }
    }
}