package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.graphics.Size;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A simple way to import {@link io.github.srcimon.screwbox.core.environment.Entity entities} into the
 * {@link Environment} from a string.
 *
 * @see Environment#importSource(Object)
 * @see Environment#importSource(List)
 * @since 2.10.0
 */
public final class AsciiMap {

    //TODO document
    public record Block(List<Tile> tiles) {

        public char value() {
            return tiles.getFirst().value();
        }

        //TODO fix that obvious
        public Bounds bounds() {
            var list = new ArrayList<>(tiles);

            list.sort(Comparator.comparing(t -> t.origin().x()));
            var minX = list.getFirst().origin().x();
            var maxX = list.getLast().origin().x() + list.getLast().bounds().width();

            list.sort(Comparator.comparing(t -> t.origin().y()));
            var minY = list.getFirst().origin().y();
            var maxY = list.getLast().origin().y() + list.getLast().bounds().height();


            return Bounds.atOrigin(minX, minY, maxX - minX, maxY - minY);
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
    public record Tile(Size size, int column, int row, char value) {

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
            importBlocks();//TODO only import when not called
        }
    }

    private void importBlocks() {
        Character currentValue = null;
        List<Tile> currentBlock = new ArrayList<>();
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                Optional<Tile> tile = tileAt(x, y);
                if (tile.isPresent()) {
                    Tile currentTile = tile.get();
                    if (currentBlock.isEmpty() || Objects.equals(currentValue, currentTile.value)) {
                        currentBlock.add(currentTile);
                        currentValue = currentTile.value;
                    } else {
                        if (currentBlock.size() > 1) {
                            blocks.add(new Block(currentBlock));
                        }
                        currentBlock = new ArrayList<>();
                        currentBlock.add(currentTile);
                        currentValue = currentTile.value;
                    }
                }
            }
            if (currentBlock.size() > 1) {
                blocks.add(new Block(currentBlock));
            }
            currentBlock = new ArrayList<>();
            currentValue = null;
        }
        // squash vertically
        List<Block> realBlocks= new ArrayList<>();
        while (!blocks.isEmpty()) {
            Block current = blocks.getFirst();

            Block tryToCombi = tryCombine(current);
            if(tryToCombi!=null) {
                blocks.add(new Block(ListUtil.combine(current.tiles, tryToCombi.tiles)));
                blocks.remove(tryToCombi);
            } else {
                realBlocks.add(current);
            }
            blocks.remove(current);

        }
        blocks.addAll(realBlocks);

    }

    private Block tryCombine(Block current) {
        for (var other : blocks) {
            var combined = GeometryUtil.tryToCombine(current.bounds(), other.bounds());
            if (combined.isPresent() && other.value() == current.value()) {
                return other;
            }
        }
        return null;
    }

    private Optional<Tile> tileAt(int x, int y) {
        return tiles.stream()
                .filter(tile -> tile.column == x)
                .filter(tile -> tile.row == y)
                .findFirst();//TODO Performance tune
    }

    private void importTiles(final String map) {
        final var lines = map.split(System.lineSeparator());
        int row = 0;
        for (final var line : lines) {
            int column = 0;
            for (final var character : line.toCharArray()) {
                tiles.add(new Tile(Size.square(this.size), column, row, character));
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
