package dev.screwbox.core.creation;

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

    private AsciiMap(final String map, int tileSize) {
        super(createTiles(map, tileSize), tileSize);
    }

    private static List<Tile<Character>> createTiles(final String map, int tileSize) {
        Objects.requireNonNull(map, "map must not be null");
        if(map.isEmpty()) {
            return Collections.emptyList();
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

        final List<Tile<Character>> createdTiles = new ArrayList<>();
        for (final var entry : directory.entrySet()) {
            final var tileOffset = entry.getKey();

            final var mask = AutoTile.createMask(tileOffset,
                    location -> entry.getValue().equals(directory.get(location)));
            createdTiles.add(new Tile<>(Size.square(tileSize), tileOffset.x(), tileOffset.y(), entry.getValue(), mask));
        }
        return createdTiles;
    }

}
