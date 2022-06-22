package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.util.Objects;

public class RasterPoint {
    public int x;
    public int y;
    public RasterPoint previous;

    public RasterPoint(int x, int y, RasterPoint previous) {
        this.x = x;
        this.y = y;
        this.previous = previous;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    @Override
    public boolean equals(Object o) {
        RasterPoint point = (RasterPoint) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public RasterPoint offset(int ox, int oy) {
        return new RasterPoint(x + ox, y + oy, this);
    }
}
