package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.util.Objects;

public class RasterPoint {
    private final int x;
    private final int y;
    private final RasterPoint previous;

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

    public RasterPoint previous() {
        return previous;
    }

    public RasterPoint offset(final int deltaX, final int deltaY) {
        return new RasterPoint(x + deltaX, y + deltaY, this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RasterPoint other = (RasterPoint) obj;
        return x == other.x && y == other.y;
    }
}
