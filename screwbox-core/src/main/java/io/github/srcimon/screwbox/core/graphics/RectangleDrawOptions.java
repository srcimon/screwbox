package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Rotation;

/**
 * Customize the drawing of rectangles.
 *
 * @see Screen#drawRectangle(ScreenBounds, RectangleDrawOptions)
 * @see Screen#drawRectangle(Offset, Size, RectangleDrawOptions)
 * @see World#drawRectangle(Bounds, RectangleDrawOptions)
 */
public record RectangleDrawOptions(Style style, Color color, int strokeWidth, Rotation rotation) {

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

    private RectangleDrawOptions(final Style style, final Color color) {
        this(style, color, 1, Rotation.none());
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
    public RectangleDrawOptions rotation(final Rotation rotation) {
        return new RectangleDrawOptions(style, color, strokeWidth, rotation);
    }
}
