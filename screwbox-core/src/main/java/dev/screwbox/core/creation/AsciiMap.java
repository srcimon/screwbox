package dev.screwbox.core.creation;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;
import dev.screwbox.core.graphics.Offset;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//TODO really change package?
//TODO move environmentImport here
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
        Objects.requireNonNull(map, "map must not be null");
        if (map.isEmpty()) {
            return new AsciiMap(Collections.emptyMap(), size);
        }
        final Map<Offset, Character> directory = new HashMap<>();

        int columns = 0;
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
        return new AsciiMap(directory, size);
    }

    private AsciiMap(Map<Offset, Character> directory, int tileSize) {
        //TODO move inside tile map
        super(directory, tileSize);
    }


}
