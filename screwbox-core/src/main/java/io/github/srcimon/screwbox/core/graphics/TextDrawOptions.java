package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Customize the drawing of texts using a {@link Pixelfont}.
 *
 * @param font        the {@link Pixelfont} used for drawing
 * @param padding     the padding between characters
 * @param scale       the scale of the {@link Sprite}
 * @param isUppercase if used, changes all characters to uppercase characters
 * @param opacity     the opacity used for drawing
 * @param alignment   the direction to draw from given offset
 */
public record TextDrawOptions(Pixelfont font, int padding, double scale, boolean isUppercase, Percent opacity,
                              Alignment alignment) {

    /**
     * Alignment of the text.
     */
    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }

    public TextDrawOptions {
        requireNonNull(font, "font must not be null");
        requireNonNull(opacity, "opacity must not be null");
        requireNonNull(alignment, "alignment must not be null");
    }

    private TextDrawOptions(final Pixelfont font) {
        this(font, 2, 1, false, Percent.max(), Alignment.LEFT);
    }


    /**
     * Creates a new instance with the given {@link Pixelfont}.
     *
     * @see #font(Pixelfont)
     */
    public static TextDrawOptions font(final Supplier<Pixelfont> font) {
        return font(font.get());
    }

    /**
     * Creates a new instance with the given {@link Pixelfont}.
     *
     * @see #font(Supplier)
     */
    public static TextDrawOptions font(final Pixelfont font) {
        return new TextDrawOptions(font);
    }

    /**
     * Creates a new instance with {@link Alignment#RIGHT}.
     */
    public TextDrawOptions alignRight() {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, Alignment.RIGHT);
    }

    /**
     * Creates a new instance with {@link Alignment#CENTER}.
     */
    public TextDrawOptions alignCenter() {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, Alignment.CENTER);
    }

    /**
     * Creates a new instance with given padding.
     */
    public TextDrawOptions padding(final int padding) {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, alignment);
    }

    /**
     * Creates a new instance with given scale.
     */
    public TextDrawOptions scale(final double scale) {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, alignment);
    }

    /**
     * Creates a new instance with all uppercase characters.
     */
    public TextDrawOptions uppercase() {
        return new TextDrawOptions(font, padding, scale, true, opacity, alignment);
    }

    /**
     * Creates a new instance with given opacity.
     */
    public TextDrawOptions opacity(final Percent opacity) {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, alignment);
    }

    /**
     * Returns the width of the given text renderd with this {@link TextDrawOptions}.
     */
    public int widthOf(final String text) {
        int totalWidth = 0;
        for (final var sprite : font.spritesFor(isUppercase ? text.toUpperCase() : text)) {
            totalWidth += (int) ((sprite.width() + padding) * scale);
        }
        return totalWidth;
    }

    /**
     * Returns the {@link Size} of the given text renderd with this {@link TextDrawOptions}.
     */
    public Size sizeOf(final String text) {
        return Size.of(widthOf(text), font.height());
    }
}
