package de.suzufa.screwbox.core.graphics;

//TODO: COMMENT
public enum FlipMode {

    HORIZONTAL(true, false),
    VERTICAL(false, true),
    BOTH(true, true),
    NONE(false, false);

    private final boolean horizontal;
    private final boolean vertical;

    private FlipMode(final boolean horizontal, final boolean vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public boolean isVertical() {
        return vertical;
    }
}
