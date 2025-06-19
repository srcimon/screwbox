package dev.screwbox.core.creation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.AutoTile;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class TileMap<T> {

    /**
     * A tile within the {@link AsciiMap}.
     *
     * @param size   width and height of the tile
     * @param column column of the tile
     * @param row    row of the tile
     * @param value  character the tile is created from
     */
    public static record Tile<T>(Size size, int column, int row, T value, AutoTile.Mask autoTileMask) {

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

    protected final List<Tile<T>> tiles = new ArrayList<>();
    protected final int tileSize;
    protected int rows;
    protected int columns;

    //TODO make size -> Size
    protected TileMap(final int tileSize) {
        Validate.positive(tileSize, "tile size must be positive");
        this.tileSize =tileSize;
    }
    /**
     * Returns all {@link Tile tiles} contained in the map.
     */
    public List<Tile<T>> tiles() {
        return Collections.unmodifiableList(tiles);
    }

    /**
     * Returns the outer {@link Bounds} that contains all {@link #tiles()}.
     */
    public Bounds bounds() {
        return Bounds.atOrigin(0, 0, (double) tileSize * columns, (double) tileSize * rows);
    }

    /**
     * Returns the {@link Tile} at the specified position. Will be empty if the position is empty.
     *
     * @since 2.20.0
     */
    public Optional<Tile<T>> tileAt(final int x, final int y) {
        return tiles.stream()
                .filter(tile -> tile.column() == x)
                .filter(tile -> tile.row() == y)
                .findFirst();
    }
}
