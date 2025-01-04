package io.github.srcimon.screwbox.core.environment;

import io.github.srcimon.screwbox.core.Vector;

public record AsciiTile(int size, int column, int row, char value) {


    public Vector position() {
        return Vector.of(size * column, size * row);
    }
}
