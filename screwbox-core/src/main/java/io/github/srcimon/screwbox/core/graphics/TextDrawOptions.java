package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

//TODO align
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

        if (padding < 0) {
            throw new IllegalArgumentException("padding must be positive");
        }
    }

    private TextDrawOptions(final Pixelfont font) {
        this(font, 1, 1, false, Percent.max(), Alignment.LEFT);
    }

    public TextDrawOptions alignRight() {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, Alignment.RIGHT);
    }

    public TextDrawOptions alignCenter() {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, Alignment.CENTER);
    }

    public static TextDrawOptions font(final Supplier<Pixelfont> font) {
        return font(font.get());
    }

    public static TextDrawOptions font(final Pixelfont font) {
        return new TextDrawOptions(font);
    }

    public TextDrawOptions padding(final int padding) {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, alignment);
    }

    public TextDrawOptions scale(final double scale) {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, alignment);
    }

    public TextDrawOptions uppercase() {
        return new TextDrawOptions(font, padding, scale, true, opacity, alignment);
    }

    public TextDrawOptions opacity(final Percent opacity) {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity, alignment);
    }
}
