package dev.screwbox.core.creation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.AutoTile;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.utils.GeometryUtil;
import dev.screwbox.core.utils.ListUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

//TODO really change package?

/**
 * A simple way to import {@link Entity entities} into the
 * {@link Environment} from a string.
 *
 * @see Environment#importSource(Object)
 * @see Environment#importSource(List)
 * @since 2.10.0
 */
public final class AsciiMap extends TileMap<Character> {

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
        super(size);
        Objects.requireNonNull(map, "map must not be null");
        if (!map.isEmpty()) {
            importTiles(map);
            createBlocksFromTiles();
            squashVerticallyAlignedBlocks();
            removeSingleTileBlocks();
        }
    }

    private void removeSingleTileBlocks() {
        blocks.removeIf(block -> block.tiles().size() == 1);
    }

    private void createBlocksFromTiles() {
        final List<Tile<Character>> currentBlock = new ArrayList<>();
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
        final List<Block<Character>> survivorBlocks = new ArrayList<>();
        while (!blocks.isEmpty()) {
            final Block<Character> current = blocks.getFirst();

            tryCombine(current).ifPresentOrElse(combined -> {
                blocks.add(new Block<>(ListUtil.combine(current.tiles(), combined.tiles())));
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
     * Returns all {@link Block blocks} contained in the map. Blocks are made of two or more {@link Tile tiles}.
     *
     * @since 2.20.0
     */
    public List<Block> blocks() {
        return Collections.unmodifiableList(blocks);
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
            tiles.add(new Tile<>(Size.square(tileSize), tileOffset.x(), tileOffset.y(), entry.getValue(), mask));

        }
    }

}
