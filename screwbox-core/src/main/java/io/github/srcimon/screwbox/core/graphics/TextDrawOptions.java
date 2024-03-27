package io.github.srcimon.screwbox.core.graphics;

import static java.util.Objects.requireNonNull;

//TODO javadoc and test

/**
 * Customize the drawing of texts with system fonts.
 *
 * @param fontName  the font name used to draw the text
 * @param size      the font size used to draw the text
 * @param isBold    draw bold text
 * @param isItalic  draw italic text
 * @param color     the {@link Color} used to draw the text
 * @param alignment the direction to draw from given offset
 * @see Screen#drawText(Offset, String, TextDrawOptions)
 */
public record TextDrawOptions(String fontName, int size, boolean isBold, boolean isItalic, Color color,
                              Alignment alignment) {

    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }

    public TextDrawOptions {
        requireNonNull(fontName, "font name must not be null");
        requireNonNull(color, "color must not be null");

        if (size < 4) {
            throw new IllegalArgumentException("font size must be at least 4");
        }
    }

    public static TextDrawOptions systemFont(final String fontName) {
        return systemFont(fontName, 10);
    }

    public static TextDrawOptions systemFont(final String fontName, final int size) {
        return new TextDrawOptions(fontName, size, false, false, Color.WHITE, Alignment.LEFT);
    }

    public TextDrawOptions alignment(final Alignment alignment) {
        return new TextDrawOptions(fontName, size, isBold, isItalic, color, alignment);
    }

    public TextDrawOptions color(final Color color) {
        return new TextDrawOptions(fontName, size, isBold, isItalic, color, alignment);
    }

    public TextDrawOptions bold() {
        return new TextDrawOptions(fontName, size, true, isItalic, color, alignment);
    }

    public TextDrawOptions italic() {
        return new TextDrawOptions(fontName, size, isBold, true, color, alignment);
    }

    public TextDrawOptions size(final int size) {
        return new TextDrawOptions(fontName, size, isBold, isItalic, color, alignment);
    }
}
