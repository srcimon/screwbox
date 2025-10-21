package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.AutoTile;
import dev.screwbox.core.graphics.Sprite;

import java.util.function.Supplier;

/**
 * Enables dynamic auto tiling for the entity. Will constantly update {@link RenderComponent} of same {@link Entity}
 * with matching {@link Sprite} from {@link AutoTile} based on adjacent {@link Entity entities} with same
 * {@link AutoTile}.
 *
 * @since 3.12.0
 */
public class AutoTileComponent implements Component {

    /**
     * {@link AutoTile} used for updating the {@link RenderComponent}.
     */
    public final AutoTile tile;

    /**
     * Last mask calculated for this {@link Entity}. Will be updated automatically.
     */
    public AutoTile.Mask mask;

    /**
     * Creates a new instance.
     */
    public AutoTileComponent(final Supplier<AutoTile> tile) {
        this.tile = tile.get();
    }

    /**
     * Creates a new instance.
     */
    public AutoTileComponent(final AutoTile tile) {
        this.tile = tile;
    }
}
