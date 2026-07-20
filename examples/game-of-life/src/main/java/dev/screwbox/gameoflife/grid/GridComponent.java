package dev.screwbox.gameoflife.grid;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.navigation.Grid;

import java.io.Serial;
import java.util.Random;

import static dev.screwbox.core.Bounds.$$;

public class GridComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Grid<Boolean> grid;

    public GridComponent() {
        final Random random = new Random();
        grid = Grid.createByCellSize(Size.square(2), $$(-1000, -1000, 1000, 1000), Boolean.class);
        for (int i = 0; i < grid.size().width() * 60; i++) {
            final int x = random.nextInt(0, grid.size().width());
            final int y = random.nextInt(0, grid.size().height());
            grid.set(x, y, true);
        }
    }
}