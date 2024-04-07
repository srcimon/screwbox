package io.github.srcimon.screwbox.core.graphics;

import static java.util.Objects.requireNonNull;

/**
 * Customize the drawing of texts with system fonts.
 *
 * @param fontName  the font name used to draw the text
 * @param size      the font size used to draw the text
 * @param isBold    draw bold text
 * @param isItalic  draw italic text
 * @param color     the {@link Color} used to draw the text
 * @param alignment the direction to draw from given offset
 * @see Screen#drawText(Offset, String, SystemTextDrawOptions)
 */
public record SystemTextDrawOptions(String fontName, int size, boolean isBold, boolean isItalic, Color color,
                                    Alignment alignment) {

    /**
     * Alignment of the text.
     */
    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }

    public SystemTextDrawOptions {
        requireNonNull(fontName, "font name must not be null");
        requireNonNull(color, "color must not be null");

        if (size < 4) {
            throw new IllegalArgumentException("font size must be at least 4");
        }
    }

    /**
     * Creates a new instance with given {@link #fontName()} and {@link #size()} 10.
     */
    public static SystemTextDrawOptions systemFont(final String fontName) {
        return systemFont(fontName, 10);
    }

    /**
     * Creates a new instance with given {@link #fontName()} and given {@link #size()}.
     */
    public static SystemTextDrawOptions systemFont(final String fontName, final int size) {
        return new SystemTextDrawOptions(fontName, size, false, false, Color.WHITE, Alignment.LEFT);
    }

    /**
     * Creates a new instance with {@link Alignment#RIGHT}.
     */
    public SystemTextDrawOptions alignRight() {
        return new SystemTextDrawOptions(fontName, size, isBold, isItalic, color, Alignment.RIGHT);
    }

    /**
     * Creates a new instance with {@link Alignment#CENTER}.
     */
    public SystemTextDrawOptions alignCenter() {
        return new SystemTextDrawOptions(fontName, size, isBold, isItalic, color, Alignment.CENTER);
    }

    /**
     * Creates a new instance with given {@link Color}.
     */
    public SystemTextDrawOptions color(final Color color) {
        return new SystemTextDrawOptions(fontName, size, isBold, isItalic, color, alignment);
    }

    /**
     * Creates a new instance with bold font.
     */
    public SystemTextDrawOptions bold() {
        return new SystemTextDrawOptions(fontName, size, true, isItalic, color, alignment);
    }

    /**
     * Creates a new instance with italic font.
     */
    public SystemTextDrawOptions italic() {
        return new SystemTextDrawOptions(fontName, size, isBold, true, color, alignment);
    }

    /**
     * Creates a new instance with given {@link #size()}.
     */
    public SystemTextDrawOptions size(final int size) {
        return new SystemTextDrawOptions(fontName, size, isBold, isItalic, color, alignment);
    }
}
