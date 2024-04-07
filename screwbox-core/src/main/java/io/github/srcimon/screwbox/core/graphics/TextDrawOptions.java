package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.Percent;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

//TODO scale
//TODO opacity
//TODO align
public record TextDrawOptions(Pixelfont font, int padding, double scale, boolean isUppercase, Percent opacity) {

    public TextDrawOptions {
        requireNonNull(font, "font must not be null");
        requireNonNull(opacity, "opacity must not be null");

        if (padding < 0) {
            throw new IllegalArgumentException("padding must be positive");
        }
        if (scale <= 0) {
            throw new IllegalArgumentException("padding must be positive");
        }
    }

    private TextDrawOptions(final Pixelfont font) {
        this(font, 1, 1, false, Percent.max());
    }

    public static TextDrawOptions font(final Supplier<Pixelfont> font) {
        return font(font.get());
    }

    public static TextDrawOptions font(final Pixelfont font) {
        return new TextDrawOptions(font);
    }

    public TextDrawOptions padding(final int padding) {
        return new TextDrawOptions(font, padding, scale ,isUppercase, opacity);
    }

    public TextDrawOptions scale(final double scale) {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity);
    }

    public TextDrawOptions uppercase() {
        return new TextDrawOptions(font, padding, scale, true, opacity);
    }

    public TextDrawOptions opacity(final Percent opacity) {
        return new TextDrawOptions(font, padding, scale, isUppercase, opacity);
    }
}
