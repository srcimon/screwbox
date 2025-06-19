package dev.screwbox.core.creation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.AutoTile;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.utils.GeometryUtil;
import dev.screwbox.core.utils.ListUtil;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

    /**
     * Blocks consist of adjacent {@link Tile tiles}. Blocks always prefer horizontal {@link Tile tiles} when created.
     *
     * @since 2.20.0
     */
    public static class Block<T> {

        private final List<Tile<T>> tiles;
        private final T value;
        private final Bounds bounds;

        protected Block(final List<Tile<T>> tiles) {
            this.tiles = List.copyOf(tiles);//TODO copy needed?
            this.value = tiles.getFirst().value();
            double minX = Double.MAX_VALUE;
            double minY = Double.MAX_VALUE;
            double maxX = Double.MIN_VALUE;
            double maxY = Double.MIN_VALUE;

            for (var tile : tiles) {
                minX = Math.min(minX, tile.bounds().minX());
                minY = Math.min(minY, tile.bounds().minY());
                maxX = Math.max(maxX, tile.bounds().maxX());
                maxY = Math.max(maxY, tile.bounds().maxY());
            }
            this.bounds = Bounds.atOrigin(minX, minY, maxX - minX, maxY - minY);
        }

        /**
         * {@link Tile Tiles} contained within the {@link Block}.
         */
        public List<Tile<T>> tiles() {
            return tiles;
        }

        /**
         * Identification value of the {@link Block}.
         */
        public T value() {
            return value;
        }

        /**
         * {@link Bounds} of the {@link Block}.
         */
        public Bounds bounds() {
            return bounds;
        }

        /**
         * Returns the {@link Size} of the {@link Block}.
         *
         * @since 3.1.0
         */
        public Size size() {
            return Size.of(bounds.width(), bounds.height());
        }
    }

    protected final List<Block<T>> blocks = new ArrayList<>();
    protected final List<Tile<T>> tiles;
    protected final int tileSize;
    protected int rows;
    protected int columns;

    //TODO make size -> Size
    protected TileMap(final List<Tile<T>> tiles, final int tileSize) {
        Validate.positive(tileSize, "tile size must be positive");
        this.tileSize = tileSize;
        this.tiles = tiles;
        for (var tile : tiles) {
            rows = Math.max(rows, tile.row);
            columns = Math.max(columns, tile.column);
        }
        createBlocksFromTiles();
        squashVerticallyAlignedBlocks();
        removeSingleTileBlocks();
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


    private void removeSingleTileBlocks() {
        blocks.removeIf(block -> block.tiles().size() == 1);
    }

    private void createBlocksFromTiles() {
        final List<Tile<T>> currentBlock = new ArrayList<>();
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                tileAt(x, y).ifPresent(currentTile -> {
                    if (!currentBlock.isEmpty() && !Objects.equals(currentBlock.getFirst().value(), currentTile.value())) {
                        blocks.add(new Block<>(currentBlock));
                        currentBlock.clear();
                    }
                    currentBlock.add(currentTile);
                });
            }
            if (!currentBlock.isEmpty()) {
                blocks.add(new Block<>(currentBlock));
                currentBlock.clear();
            }
        }
    }

    private void squashVerticallyAlignedBlocks() {
        final List<Block<T>> survivorBlocks = new ArrayList<>();
        while (!blocks.isEmpty()) {
            final Block<T> current = blocks.getFirst();

            tryCombine(current).ifPresentOrElse(combined -> {
                blocks.add(new Block<>(ListUtil.combine(current.tiles(), combined.tiles())));
                blocks.remove(combined);
            }, () -> survivorBlocks.add(current));
            blocks.remove(current);

        }
        blocks.addAll(survivorBlocks);
    }

    private Optional<Block<T>> tryCombine(final Block<T> current) {
        for (var other : blocks) {
            if (other.value() == current.value() && GeometryUtil.tryToCombine(current.bounds(), other.bounds()).isPresent()) {
                return Optional.of(other);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns all {@link Block blocks} contained in the map. Blocks are made of two or more {@link Tile tiles}.
     *
     * @since 2.20.0
     */
    public List<Block<T>> blocks() {
        return Collections.unmodifiableList(blocks);
    }
}
