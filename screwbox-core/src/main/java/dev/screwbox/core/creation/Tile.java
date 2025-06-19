package dev.screwbox.core.creation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.AutoTile;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;

import java.util.function.Supplier;

//TODO fixup javadoc
/**
 * A tile within the {@link AsciiMap}.
 *
 * @param size   width and height of the tile
 * @param column column of the tile
 * @param row    row of the tile
 * @param value  character the tile is created from
 */
public record Tile<T>(Size size, int column, int row, T value, AutoTile.Mask autoTileMask) {

    /**
     * Origin of the tile within the {@link Environment}.
     */
    public Vector origin() {
        return Vector.of((double) size.width() * column, (double) size.height() * row);
    }

    /**
     * {@link Bounds} of the tile within the {@link Environment}.
     */
    public Bounds bounds() {
        return Bounds.atOrigin(origin(), size.width(), size.height());
    }

    /**
     * Returns the position of the tile within the {@link Environment}.
     *
     * @since 2.11.0
     */
    public Vector position() {
        return bounds().position();
    }

    /**
     * Returns a {@link Sprite} from the specified {@link AutoTile} matching this
     * map position. Tiles having same value will count as connected tiles.
     *
     * @since 3.5.0
     */
    public Sprite findSprite(final Supplier<AutoTile> autoTile) {
        return findSprite(autoTile.get());
    }

    /**
     * Returns a {@link Sprite} from the specified {@link AutoTile} matching this
     * map position. Tiles having same value will count as connected tiles.
     *
     * @since 3.5.0
     */
    public Sprite findSprite(final AutoTile autoTile) {
        return autoTile.findSprite(autoTileMask);
    }

}
