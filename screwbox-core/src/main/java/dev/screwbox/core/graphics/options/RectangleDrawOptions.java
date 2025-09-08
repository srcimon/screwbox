package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Angle;
import dev.screwbox.core.graphics.Canvas;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.World;
import dev.screwbox.core.utils.Validate;

/**
 * Customize the drawing of rectangles.
 *
 * @see Canvas#drawRectangle(ScreenBounds, RectangleDrawOptions)
 * @see Canvas#drawRectangle(Offset, Size, RectangleDrawOptions)
 * @see World#drawRectangle(Bounds, RectangleDrawOptions)
 */
public record RectangleDrawOptions(Style style, Color color, int strokeWidth, Angle rotation) {

    /**
     * The style used to draw.
     */
    public enum Style {

        /**
         * Draws a filled form.
         */
        FILLED,

        /**
         * Draws only the outline using {@link RectangleDrawOptions#strokeWidth()}.
         */
        OUTLINE
    }

    public RectangleDrawOptions {
        if (Style.OUTLINE != style && strokeWidth != 1) {
            throw new IllegalArgumentException("stroke width is only used when drawing outline of rectangles");
        }
        Validate.positive(strokeWidth, "stroke width must be positive");
    }

    private RectangleDrawOptions(final Style style, final Color color) {
        this(style, color, 1, Angle.none());
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
        return new RectangleDrawOptions(style, color, strokeWidth, rotation);
    }

    /**
     * Sets the {@link #rotation()} of the drawn rectangle.
     */
    public RectangleDrawOptions rotation(final Angle rotation) {
        return new RectangleDrawOptions(style, color, strokeWidth, rotation);
    }
}
