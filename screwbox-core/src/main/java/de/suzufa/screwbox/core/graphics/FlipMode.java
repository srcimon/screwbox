package de.suzufa.screwbox.core.graphics;

/**
 * Returns the vertical and or horizontal flip (mirror) mode for an
 * {@link Sprite}.
 */
public enum FlipMode {

    /**
     * flipped horizontally
     */
    HORIZONTAL(true, false),

    /**
     * flipped vertically
     */
    VERTICAL(false, true),

    /**
     * flipped horizontally and vertically
     */
    BOTH(true, true),

    /**
     * not flipped
     */
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

    /**
     * Returns the {@link FlipMode} with inverted vertical component.
     * 
     * @return the inverted {@link FlipMode}
     */
    public FlipMode invertVertical() {
        if (isHorizontal()) {
            return isVertical() ? HORIZONTAL : BOTH;
        }

        return isVertical() ? NONE : VERTICAL;
    }

}
