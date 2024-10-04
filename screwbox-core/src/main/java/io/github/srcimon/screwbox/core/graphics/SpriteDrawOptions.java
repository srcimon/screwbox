package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;

import java.io.Serializable;

/**
 * Customize the drawing of {@link Sprite}s.
 *
 * @param scale            the scale of the {@link Sprite}
 * @param opacity          the opacity of the {@link Sprite}
 * @param rotation         the {@link Rotation} of the {@link Sprite}
 * @param isFlipHorizontal is the {@link Sprite} flipped horizontally
 * @param isFlipVertical   is the {@link Sprite} flipped vertically
 * @see Screen#drawSprite(Sprite, Offset, SpriteDrawOptions)
 */
//TODO document horizontalSpin
//TODO changelog horizontalSpin
//TODO test horizontalSpin
//TODO support verticalSpin
public record SpriteDrawOptions(double scale, Percent opacity, Rotation rotation, boolean isFlipHorizontal,
                                boolean isFlipVertical, Percent spin, boolean isSpinHorizontal) implements Serializable {

    private SpriteDrawOptions(final double scale) {
        this(scale, Percent.max(), Rotation.none(), false, false, Percent.zero(), true);
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
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical, spin, isSpinHorizontal);
    }

    /**
     * Creates a new instance with updated {@link #opacity()}.
     */
    public SpriteDrawOptions opacity(final Percent opacity) {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical, spin, isSpinHorizontal);
    }

    /**
     * Creates a new instance with updated {@link #opacity()}.
     */
    public SpriteDrawOptions opacity(final double opacity) {
        return opacity(Percent.of(opacity));
    }

    /**
     * Creates a new instance with updated {@link #rotation()}.
     */
    public SpriteDrawOptions rotation(final Rotation rotation) {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical, spin, isSpinHorizontal);
    }

    /**
     * Creates a new instance with updated {@link #isFlipHorizontal()}.
     */
    public SpriteDrawOptions flipHorizontal(final boolean flipHorizontal) {
        return new SpriteDrawOptions(scale, opacity, rotation, flipHorizontal, isFlipVertical, spin, isSpinHorizontal);
    }

    /**
     * Creates a new instance with updated {@link #isFlipVertical()}.
     */
    public SpriteDrawOptions flipVertical(final boolean flipVertical) {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, flipVertical, spin, isSpinHorizontal);
    }

    /**
     * Creates a new instance with inverted value for {@link #isFlipVertical()}.
     */
    public SpriteDrawOptions invertVerticalFlip() {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, !isFlipVertical, spin, isSpinHorizontal);
    }

    //TODO javadoc
    public SpriteDrawOptions spin(final Percent spin) {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical, spin, isSpinHorizontal);
    }

    //TODO javadoc
    public SpriteDrawOptions isSpinHorizontal(final boolean isSpinHorizontal) {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical, spin, isSpinHorizontal);
    }
}
