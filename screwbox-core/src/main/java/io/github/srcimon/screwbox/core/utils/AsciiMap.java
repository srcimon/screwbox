package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A simple way to import {@link io.github.srcimon.screwbox.core.environment.Entity entities} into the
 * {@link Environment} from a string.
 *
 * @see Environment#importSource(Object)
 * @see Environment#importSource(List)
 * @since 2.10.0
 */
public final class AsciiMap {

    public record Block(int width, int height, int offsetColumn, int offsetRow, char value, int size) {
        //TODO list of tiles
        public Vector origin() {
            return Vector.of((double) size * offsetColumn, (double) size * offsetRow);
        }

        /**
         * {@link Bounds} of the tile within the {@link Environment}.
         */
        public Bounds bounds() {
            return Bounds.atOrigin(origin(), width, height);
        }

        /**
         * Returns the position of the tile within the {@link Environment}.
         *
         * @since 2.11.0
         */
        public Vector position() {
            return bounds().position();
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
    public record Tile(int size, int column, int row, char value) {

        /**
         * Origin of the tile within the {@link Environment}.
         */
        public Vector origin() {
            return Vector.of((double) size * column, (double) size * row);
        }

        /**
         * {@link Bounds} of the tile within the {@link Environment}.
         */
        public Bounds bounds() {
            return Bounds.atOrigin(origin(), size, size);
        }

        /**
         * Returns the position of the tile within the {@link Environment}.
         *
         * @since 2.11.0
         */
        public Vector position() {
            return bounds().position();
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
        }
    }

    private void importTiles(final String map) {
        var lines = map.split(System.lineSeparator());
        int row = 0;
        for (final var line : lines) {
            int column = 0;
            for (final var character : line.toCharArray()) {
                tiles.add(new Tile(this.size, column, row, character));
                column++;
                if (column > columns) {
                    columns = column;
                }
            }
            row++;
        }
        rows = row;
    }

    /**
     * Returns all {@link Tile tiles} contained in the map. Every character within the map will be a tile.
     */
    public List<Tile> tiles() {
        return Collections.unmodifiableList(tiles);
    }
//TODO prefer horizontal vertical chunks?
    public List<Block> blocks() {
        return Collections.unmodifiableList(blocks);
    }

    /**
     * Returns the outer {@link Bounds} that contains all {@link #tiles()}.
     */
    public Bounds bounds() {
        return Bounds.atOrigin(0, 0, (double) size * columns, (double) size * rows);
    }
}
