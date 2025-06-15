package dev.screwbox.core.utils;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.AutoTile;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A simple way to import {@link Entity entities} into the
 * {@link Environment} from a string.
 *
 * @see Environment#importSource(Object)
 * @see Environment#importSource(List)
 * @since 2.10.0
 */
public final class AsciiMap {

    /**
     * Blocks consist of adjacent {@link Tile tiles}. Blocks always prefer horizontal {@link Tile tiles} when created.
     *
     * @since 2.20.0
     */
    public static class Block {

        private final List<Tile> tiles;
        private final char value;
        private final Bounds bounds;

        private Block(final List<Tile> tiles) {
            this.tiles = List.copyOf(tiles);
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
        public List<Tile> tiles() {
            return tiles;
        }

        /**
         * Identification value of the {@link Block}.
         */
        public char value() {
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

    /**
     * A tile within the {@link AsciiMap}.
     *
     * @param size   width and height of the tile
     * @param column column of the tile
     * @param row    row of the tile
     * @param value  character the tile is created from
     */
    public record Tile(Size size, int column, int row, char value, AutoTile.Mask autoTileMask) {

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

    private final List<Tile> tiles = new ArrayList<>();
    private final List<Block> blocks = new ArrayList<>();
    private final int size;
    private int rows;
    private int columns;

    /**
     * Creates an {@link AsciiMap} from a text. Uses width and height of 16 for every {@link Tile}.
     * <pre>
     * {@code
     * AsciiMap map = AsciiMap.fromString("""
     *   #############
     *   #           #
     *   #           #
     *   # x         #
     *   #############
     *   """);
     * }
     * </pre>
     *
     * @param map text that represents the map. Choose your own characters for any content you want. Line feeds create vertical tiles.
     * @see #fromString(String, int)
     */
    public static AsciiMap fromString(final String map) {
        return fromString(map, 16);
    }

    /**
     * Creates an {@link AsciiMap} from a text.
     * <pre>
     * {@code
     * AsciiMap map = AsciiMap.fromString("""
     *   #############
     *   #           #
     *   #           #
     *   # x         #
     *   #############
     *   """, 8);
     * }
     * </pre>
     *
     * @param map  text that represents the map. Choose your own characters for any content you want. Line feeds create vertical tiles.
     * @param size size of a single tile (width and height)
     */
    public static AsciiMap fromString(final String map, final int size) {
        return new AsciiMap(map, size);
    }

    private AsciiMap(final String map, int size) {
        Validate.positive(size, "size must be positive");
        Objects.requireNonNull(map, "map must not be null");
        this.size = size;
        if (!map.isEmpty()) {
            importTiles(map);
            createBlocksFromTiles();
            squashVerticallyAlignedBlocks();
            removeSingleTileBlocks();
        }
    }

    private void removeSingleTileBlocks() {
        blocks.removeIf(block -> block.tiles.size() == 1);
    }

    private void createBlocksFromTiles() {
        final List<Tile> currentBlock = new ArrayList<>();
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                tileAt(x, y).ifPresent(currentTile -> {
                    if (!currentBlock.isEmpty() && !Objects.equals(currentBlock.getFirst().value, currentTile.value)) {
                        blocks.add(new Block(currentBlock));
                        currentBlock.clear();
                    }
                    currentBlock.add(currentTile);
                });
            }
            if (!currentBlock.isEmpty()) {
                blocks.add(new Block(currentBlock));
                currentBlock.clear();
            }
        }
    }

    private void squashVerticallyAlignedBlocks() {
        final List<Block> survivorBlocks = new ArrayList<>();
        while (!blocks.isEmpty()) {
            final Block current = blocks.getFirst();

            tryCombine(current).ifPresentOrElse(combined -> {
                blocks.add(new Block(ListUtil.combine(current.tiles, combined.tiles)));
                blocks.remove(combined);
            }, () -> survivorBlocks.add(current));
            blocks.remove(current);

        }
        blocks.addAll(survivorBlocks);
    }

    private Optional<Block> tryCombine(final Block current) {
        for (var other : blocks) {
            if (other.value() == current.value() && GeometryUtil.tryToCombine(current.bounds(), other.bounds()).isPresent()) {
                return Optional.of(other);
            }
        }
        return Optional.empty();
    }

    /**
     * Returns the {@link Tile} at the specified position. Will be empty if the position is empty.
     *
     * @since 2.20.0
     */
    public Optional<Tile> tileAt(final int x, final int y) {
        return tiles.stream()
                .filter(tile -> tile.column == x)
                .filter(tile -> tile.row == y)
                .findFirst();
    }

    /**
     * Returns all {@link Tile tiles} contained in the map. Every character within the map will be a tile.
     */
    public List<Tile> tiles() {
        return Collections.unmodifiableList(tiles);
    }

    /**
     * Returns all {@link Block blocks} contained in the map. Blocks are made of two or more {@link Tile tiles}.
     *
     * @since 2.20.0
     */
    public List<Block> blocks() {
        return Collections.unmodifiableList(blocks);
    }

    /**
     * Returns the outer {@link Bounds} that contains all {@link #tiles()}.
     */
    public Bounds bounds() {
        return Bounds.atOrigin(0, 0, (double) size * columns, (double) size * rows);
    }

    private void importTiles(final String map) {
        final Map<Offset, Character> directory = new HashMap<>();

        final var lines = map.split(System.lineSeparator());
        int row = 0;
        for (final var line : lines) {
            int column = 0;
            for (final var character : line.toCharArray()) {
                directory.put(Offset.at(column, row), character);
                column++;
                if (column > columns) {
                    columns = column;
                }
            }
            row++;
        }
        rows = row;

        for (final var entry : directory.entrySet()) {
            final var tileOffset = entry.getKey();

            final var mask = AutoTile.createMask(tileOffset,
                    location -> entry.getValue().equals(directory.get(location)));
            tiles.add(new Tile(Size.square(size), tileOffset.x(), tileOffset.y(), entry.getValue(), mask));

        }
    }

}
