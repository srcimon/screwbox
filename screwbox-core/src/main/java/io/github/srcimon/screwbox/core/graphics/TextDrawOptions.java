package io.github.srcimon.screwbox.core.graphics;

import static java.util.Objects.requireNonNull;

//TODO javadoc and test
public record TextDrawOptions(String fontName, int size, boolean bold, boolean italic, Color color,
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

    public static TextDrawOptions font(final String fontName) {
        return font(fontName, 10);
    }

    public static TextDrawOptions font(final String fontName, final int size) {
        return new TextDrawOptions(fontName, size, false, false, Color.WHITE, Alignment.LEFT);
    }

    public TextDrawOptions alignment(final Alignment alignment) {
        return new TextDrawOptions(fontName, size, bold, italic, color, alignment);
    }

    public TextDrawOptions color(final Color color) {
        return new TextDrawOptions(fontName, size, bold, italic, color, alignment);
    }

    public TextDrawOptions styleBold() {
        return new TextDrawOptions(fontName, size, true, italic, color, alignment);
    }

    public TextDrawOptions styleItalic() {
        return new TextDrawOptions(fontName, size, bold, true, color, alignment);
    }

    public TextDrawOptions size(final int size) {
        return new TextDrawOptions(fontName, size, bold, italic, color, alignment);
    }
}
