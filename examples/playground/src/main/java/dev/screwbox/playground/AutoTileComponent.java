package dev.screwbox.playground;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.AutoTile;

import java.util.function.Supplier;

public class AutoTileComponent implements Component {

    public final AutoTile tile;
    public AutoTile.Mask mask;

    public AutoTileComponent(final Supplier<AutoTile> tile) {
        this.tile = tile.get();
    }

    public AutoTileComponent(final AutoTile tile) {
        this.tile = tile;
    }
}
