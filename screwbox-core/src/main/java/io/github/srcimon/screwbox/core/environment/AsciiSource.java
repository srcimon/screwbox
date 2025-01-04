package io.github.srcimon.screwbox.core.environment;

import io.github.srcimon.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AsciiSource {

    //TODO javadoc
//TODO since
//TODO inline?
//TODO fix name


    private final String level;
    private final int size;

    public AsciiSource(final String level, int size) {
        Validate.positive(size, "size must be positive");
        this.level = Objects.requireNonNull(level, "level must not be null");
        this.size = size;
    }

    public List<AsciiTile> tiles() {
        final var tiles = new ArrayList<AsciiTile>();

        var lines = level.split(System.lineSeparator());
        int row = 0;
        for(final var line : lines) {
            int column = 0;
            for(final var character : line.toCharArray()) {
                tiles.add(new AsciiTile(size, column, row, character));
                column++;
            }
            row++;
        }
        return  tiles;
    }
}
