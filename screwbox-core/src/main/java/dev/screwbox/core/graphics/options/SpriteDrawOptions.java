package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.Sprite;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Customize the drawing of {@link Sprite}s.
 *
 * @param scale                 the scale of the {@link Sprite}
 * @param opacity               the opacity of the {@link Sprite}
 * @param rotation              the {@link Angle} of the {@link Sprite}
 * @param isFlipHorizontal      is the {@link Sprite} flipped horizontally
 * @param isFlipVertical        is the {@link Sprite} flipped vertically
 * @param spin                  spins the {@link Sprite} with a pseudo 3d effect
 * @param isSpinHorizontal      switch spin of the {@link Sprite} between vertical or horizontal
 * @param shaderSetup           {@link ShaderSetup} used for drawing
 * @param zIndex                used to sort {@link Sprite sprites} orthographic within the same draw order
 * @param isIgnoreOverlayShader the {@link GraphicsConfiguration#overlayShader()} won't be applied to this {@link Sprite}
 * @see Canvas#drawSprite(Sprite, Offset, SpriteDrawOptions)
 */
public record SpriteDrawOptions(double scale, Percent opacity, Angle rotation, boolean isFlipHorizontal,
                                boolean isFlipVertical, Percent spin,
                                boolean isSpinHorizontal, int zIndex,
                                ShaderSetup shaderSetup, boolean isIgnoreOverlayShader,
                                int drawOrder) implements Serializable {


    private SpriteDrawOptions(final double scale) {
        this(scale, Percent.max(), Angle.none(), false, false, Percent.zero(), true, Integer.MIN_VALUE, null, false, 0);
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
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical, spin, isSpinHorizontal, zIndex, shaderSetup, isIgnoreOverlayShader, drawOrder);
    }

    /**
     * Creates a new instance with updated {@link #opacity()}.
     */
    public SpriteDrawOptions opacity(final Percent opacity) {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical, spin, isSpinHorizontal, zIndex, shaderSetup, isIgnoreOverlayShader, drawOrder);
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
    public SpriteDrawOptions rotation(final Angle rotation) {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical, spin, isSpinHorizontal, zIndex, shaderSetup, isIgnoreOverlayShader, drawOrder);
    }

    /**
     * Creates a new instance with updated {@link #isFlipHorizontal()}.
     */
    public SpriteDrawOptions flipHorizontal(final boolean flipHorizontal) {
        return new SpriteDrawOptions(scale, opacity, rotation, flipHorizontal, isFlipVertical, spin, isSpinHorizontal, zIndex, shaderSetup, isIgnoreOverlayShader, drawOrder);
    }

    /**
     * Creates a new instance with updated {@link #isFlipVertical()}.
     */
    public SpriteDrawOptions flipVertical(final boolean flipVertical) {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, flipVertical, spin, isSpinHorizontal, zIndex, shaderSetup, isIgnoreOverlayShader, drawOrder);
    }

    /**
     * Creates a new instance with inverted value for {@link #isFlipVertical()}.
     */
    public SpriteDrawOptions invertVerticalFlip() {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, !isFlipVertical, spin, isSpinHorizontal, zIndex, shaderSetup, isIgnoreOverlayShader, drawOrder);
    }

    /**
     * Creates a new instance with specified value for {@link #spin()}. Spin is used to create a pseudo 3d rotation effect.
     * A {@link Sprite} can either spin horizontal or vertical.
     *
     * @see #spinHorizontal(boolean)
     */
    public SpriteDrawOptions spin(final Percent spin) {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical, spin, isSpinHorizontal, zIndex, shaderSetup, isIgnoreOverlayShader, drawOrder);
    }

    /**
     * Creates a new instance with specified direction of spin. Spin is used to create a pseudo 3d rotation effect.
     * A {@link Sprite} can either spin horizontal or vertical.
     *
     * @see #spin(Percent)
     */
    public SpriteDrawOptions spinHorizontal(final boolean isSpinHorizontal) {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical, spin, isSpinHorizontal, zIndex, shaderSetup, isIgnoreOverlayShader, drawOrder);
    }

    //TODO changelog
    /**
     * Specify the z-Index for drawing orthographic within the same draw order.
     *
     * @since 3.14.0
     */
    public SpriteDrawOptions zIndex(final int zIndex) {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical, spin, isSpinHorizontal, zIndex, shaderSetup, isIgnoreOverlayShader, drawOrder);
    }

    /**
     * Sets {@link ShaderSetup} that should be applied on the {@link Sprite} when drawn.
     *
     * @since 2.15.0
     */
    public SpriteDrawOptions shaderSetup(final ShaderSetup shaderSetup) {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical, spin, isSpinHorizontal, zIndex, shaderSetup, isIgnoreOverlayShader, drawOrder);
    }

    /**
     * The {@link GraphicsConfiguration#overlayShader()} won't be applied to this {@link Sprite}.
     *
     * @since 2.17.0
     */
    public SpriteDrawOptions ignoreOverlayShader() {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical, spin, isSpinHorizontal, zIndex, shaderSetup, true, drawOrder);
    }

    /**
     * Sets {@link ShaderSetup} that should be applied on the {@link Sprite} when drawn.
     *
     * @since 2.15.0
     */
    public SpriteDrawOptions shaderSetup(final Supplier<ShaderSetup> shaderOptions) {
        return shaderSetup(shaderOptions.get());
    }

    /**
     * Specify the order of drawing.
     *
     * @since 3.14.0
     */
    public SpriteDrawOptions drawOrder(final int drawOrder) {
        return new SpriteDrawOptions(scale, opacity, rotation, isFlipHorizontal, isFlipVertical, spin, isSpinHorizontal, zIndex, shaderSetup, isIgnoreOverlayShader, drawOrder);
    }
}
