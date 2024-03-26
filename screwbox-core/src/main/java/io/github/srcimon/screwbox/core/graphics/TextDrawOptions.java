package io.github.srcimon.screwbox.core.graphics;

import static java.util.Objects.requireNonNull;

//TODO javadoc and test
public record TextDrawOptions(String fontName, int size, Style style, Color color) {

    public enum Style {
        NORMAL,
        ITALIC,
        BOLD,
        ITALIC_BOLD
    }

    public TextDrawOptions {
        requireNonNull(fontName, "font name must not be null");
        requireNonNull(color, "color must not be null");

        if (size < 4) {
            throw new IllegalArgumentException("font size must be at least 4");
        }
    }

    public static TextDrawOptions font(final String fontName) {
        return new TextDrawOptions(fontName, 10, Style.NORMAL, Color.WHITE);
    }
}
