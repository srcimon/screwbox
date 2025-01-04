package io.github.srcimon.screwbox.core.utils;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AsciiMap {

    //TODO javadoc
//TODO since

    public record Tile(int size, int column, int row, char value) {


        public Vector origin() {
            return Vector.of(size * column, size * row);
        }

        public Bounds bounds() {
            return Bounds.atOrigin(origin(), size , size );
        }
    }


    private final String map;
    private final int size;
    private int columns;
    private int rows;
    private List<Tile> tiles = new ArrayList<>();

    public static AsciiMap fromString(final String map, final int size) {
        return new AsciiMap(map, size);
    }

    private AsciiMap(final String map, int size) {
        Validate.positive(size, "size must be positive");
        this.map = Objects.requireNonNull(map, "map must not be null");
        this.size = size;

        var lines = map.split(System.lineSeparator());
        int row = 0;
        for (final var line : lines) {
            int column = 0;
            for (final var character : line.toCharArray()) {
                tiles.add(new Tile(size, column, row, character));
                column++;
                if (column > columns) {
                    columns = column;
                }
            }
            row++;
        }
        rows = row;

    }

    public List<Tile> tiles() {
        return tiles;
    }

    public Bounds bounds() {
        return Bounds.atOrigin(0, 0, size * columns, size * rows);
    }
}
