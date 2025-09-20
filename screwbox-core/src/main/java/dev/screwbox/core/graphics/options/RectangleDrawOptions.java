package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.utils.Validate;

/**
 * Customization options for drawing of rectangles.
 *
 * @param color       {@link Color} used for drawing
 * @param rotation    rotation used for drawing
 * @param style       general style used for drawing
 * @param strokeWidth stroke width used when using {@link Style#OUTLINE}
 * @see Canvas#drawRectangle(ScreenBounds, RectangleDrawOptions)
 * @see Canvas#drawRectangle(Offset, Size, RectangleDrawOptions)
 * @see World#drawRectangle(Bounds, RectangleDrawOptions)
 */
//TODO document curveRadius
//TODO validate curveRadius
public record RectangleDrawOptions(Style style, Color color, int strokeWidth, Angle rotation, int curveRadius) {

    /**
     * The style used to draw.
     */
    public enum Style {

        /**
         * Draw using a single {@link Color}.
         */
        FILLED,

        /**
         * Draw only the outline using {@link RectangleDrawOptions#strokeWidth()}.
         */
        OUTLINE,

        //TODO document and test
        FADING
    }

    public RectangleDrawOptions {
        Validate.isFalse(() -> Style.OUTLINE != style && strokeWidth != 1, "stroke width is only used when drawing outline of rectangles");
        Validate.positive(strokeWidth, "stroke width must be positive");
    }

    private RectangleDrawOptions(final Style style, final Color color) {
        this(style, color, 1, Angle.none(), 0);
    }

    public static RectangleDrawOptions fading(final Color color) {
        return new RectangleDrawOptions(Style.FADING, color);
    }

    /**
     * Draw a filled rectangle with the given {@link Color}.
     */
    public static RectangleDrawOptions filled(final Color color) {
        return new RectangleDrawOptions(Style.FILLED, color);
    }

    /**
     * Draw only the outline with the given {@link Color}.
     */
    public static RectangleDrawOptions outline(final Color color) {
        return new RectangleDrawOptions(Style.OUTLINE, color);
    }

    /**
     * Sets the {@link #strokeWidth()} when drawing {@link #outline(Color)}. Not used when using {@link #filled(Color)}.
     */
    public RectangleDrawOptions strokeWidth(final int strokeWidth) {
        return new RectangleDrawOptions(style, color, strokeWidth, rotation, curveRadius);
    }

    /**
     * Sets the {@link #rotation()} of the drawn rectangle.
     */
    public RectangleDrawOptions rotation(final Angle rotation) {
        return new RectangleDrawOptions(style, color, strokeWidth, rotation, curveRadius);
    }

    //TODO document
    public RectangleDrawOptions curveRadius(final int curveRadius) {
        return new RectangleDrawOptions(style, color, strokeWidth, rotation, curveRadius);
    }
}
