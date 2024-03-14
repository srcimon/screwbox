package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;

//TODO rectangle immutable
//TODO line immutable
//TODO circle immutable
//TODO: camerashakeoptions immutable

/**
 * Customize the drawing of {@link Sprite}s.
 * @param scale the scale of the {@link Sprite}
 * @param opacity the opacity of the {@link Sprite}
 * @param rotation the {@link Rotation} of the {@link Sprite}
 * @param flip the {@link Flip} of the {@link Sprite}
 *             
 * @see Screen#drawSprite(Sprite, Offset, SpriteDrawOptions)
 */
public record SpriteDrawOptions(double scale, Percent opacity, Rotation rotation, Flip flip) {

    /**
     * Returns the vertical and or horizontal flip (mirror) mode for an
     * {@link Sprite}.
     */
    public enum Flip {

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

        Flip(final boolean horizontal, final boolean vertical) {
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
         * Returns the {@link Flip} with inverted vertical component.
         *
         * @return the inverted {@link Flip}
         */
        public Flip invertVertical() {
            if (isHorizontal()) {
                return isVertical() ? HORIZONTAL : BOTH;
            }

            return isVertical() ? NONE : VERTICAL;
        }
    }

    private SpriteDrawOptions(final double scale) {
        this(scale, Percent.max(), Rotation.none(), Flip.NONE);
    }

    /**
     * Creates a new instance with {@link #scale()} 1.
     */
    public static SpriteDrawOptions originalSize() {
        return scaled(1);
    }

    /**
     * Creates a new instance with given {@link #scale()}.
     */
    public static SpriteDrawOptions scaled(double scale) {
        return new SpriteDrawOptions(scale);
    }

    /**
     * Creates a new instance with updated {@link #scale()}.
     */
    public SpriteDrawOptions scale(final double scale) {
        return new SpriteDrawOptions(scale, opacity, rotation, flip);
    }

    /**
     * Creates a new instance with updated {@link #opacity()}.
     */
    public SpriteDrawOptions opacity(final Percent opacity) {
        return new SpriteDrawOptions(scale, opacity, rotation, flip);
    }

    /**
     * Creates a new instance with updated {@link #rotation()}.
     */
    public SpriteDrawOptions rotation(final Rotation rotation) {
        return new SpriteDrawOptions(scale, opacity, rotation, flip);
    }

    /**
     * Creates a new instance with updated {@link Flip}.
     */
    public SpriteDrawOptions flip(final Flip flip) {
        return new SpriteDrawOptions(scale, opacity, rotation, flip);
    }
}
