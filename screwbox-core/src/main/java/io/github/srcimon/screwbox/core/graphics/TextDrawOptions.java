package io.github.srcimon.screwbox.core.graphics;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

//TODO scale
//TODO opacity
//TODO uppercase()
//TODO lowercase()
//TODO characterRotation
//TODO align
//TODO spacing
//TODO padding (=spacing?)
public record TextDrawOptions(Pixelfont font) {

    public TextDrawOptions {
        requireNonNull(font, "font must not be null");
    }

    public static TextDrawOptions font(final Supplier<Pixelfont> font) {
        return font(font.get());
    }

    public static TextDrawOptions font(final Pixelfont font) {
        return new TextDrawOptions(font);
    }
}
