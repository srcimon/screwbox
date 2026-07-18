package dev.screwbox.core.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.graphics.Offset;

import java.util.ArrayList;
import java.util.List;

//TODO get rid completly!

/**
 * Stores binary data within cells that are aligned to the game world.
 */
public class BinaryGrid extends Grid<Boolean> {

    public BinaryGrid(final Bounds bounds, final int cellSize) {
        super(bounds, cellSize);
    }


}