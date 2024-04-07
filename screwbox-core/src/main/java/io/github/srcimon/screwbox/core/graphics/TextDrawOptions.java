package io.github.srcimon.screwbox.core.graphics;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

//TODO scale
//TODO opacity
//TODO characterRotation
//TODO align
//TODO spacing
//TODO padding (=spacing?)
public record TextDrawOptions(Pixelfont font, int padding, double scale, boolean isUppercase) {

    public TextDrawOptions {
        requireNonNull(font, "font must not be null");
        if (padding < 0) {
            throw new IllegalArgumentException("padding must be positive");
        }
        if (scale <= 0) {
            throw new IllegalArgumentException("padding must be positive");
        }
    }

    private TextDrawOptions(final Pixelfont font) {
        this(font, 1, 1, false);
    }

    public static TextDrawOptions font(final Supplier<Pixelfont> font) {
        return font(font.get());
    }

    public static TextDrawOptions font(final Pixelfont font) {
        return new TextDrawOptions(font);
    }

    public TextDrawOptions padding(final int padding) {
        return new TextDrawOptions(font, padding, scale ,isUppercase);
    }

    public TextDrawOptions scale(final double scale) {
        return new TextDrawOptions(font, padding, scale, isUppercase);
    }

    public TextDrawOptions uppercase() {
        return new TextDrawOptions(font, padding, scale, true);
    }
}
