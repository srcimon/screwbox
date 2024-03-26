package io.github.srcimon.screwbox.core.graphics;

import static java.util.Objects.requireNonNull;

//TODO javadoc and test
public record TextDrawOptions(String fontName, int size, Style style, Color color, Alignment alignment) {

    public enum Style {
        NORMAL,
        ITALIC,
        BOLD,
        ITALIC_BOLD
    }

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
    public static TextDrawOptions font(final String fontName) {
        return font(fontName, 10);
    }

    public static TextDrawOptions font(final String fontName, final int size) {
        return new TextDrawOptions(fontName, size, Style.NORMAL, Color.WHITE, Alignment.LEFT);
    }

    public TextDrawOptions alignment(final Alignment alignment) {
        return new TextDrawOptions(fontName, size, style, color, alignment);
    }

    public TextDrawOptions color(final Color color) {
        return new TextDrawOptions(fontName, size, style, color, alignment);
    }

    public TextDrawOptions style(final Style style) {
        return new TextDrawOptions(fontName, size, style, color, alignment);
    }

    public TextDrawOptions size(final int size) {
        return new TextDrawOptions(fontName, size, style, color, alignment);
    }
}
