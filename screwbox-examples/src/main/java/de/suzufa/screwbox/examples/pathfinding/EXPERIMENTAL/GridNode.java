package de.suzufa.screwbox.examples.pathfinding.EXPERIMENTAL;

import java.util.Objects;

public class GridNode {
    private final int x;
    private final int y;
    private final GridNode previous;

    public GridNode(final int x, final int y, final GridNode previous) {
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

    public GridNode previous() {
        return previous;
    }

    public GridNode offset(final int deltaX, final int deltaY) {
        return new GridNode(x + deltaX, y + deltaY, this);
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
        GridNode other = (GridNode) obj;
        return x == other.x && y == other.y;
    }
}
