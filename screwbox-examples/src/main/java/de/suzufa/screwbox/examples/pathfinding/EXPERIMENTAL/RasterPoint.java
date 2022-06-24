package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.util.Objects;

public class RasterPoint {
    private final int x;
    private final int y;
    public RasterPoint previous;

    public RasterPoint(final int x, final int y, final RasterPoint previous) {
        this.x = x;
        this.y = y;
        this.previous = previous;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    @Override
    public boolean equals(final Object o) {
        final RasterPoint point = (RasterPoint) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public RasterPoint offset(final int deltaX, final int deltaY) {
        return new RasterPoint(x + deltaX, y + deltaY, this);
    }
}
