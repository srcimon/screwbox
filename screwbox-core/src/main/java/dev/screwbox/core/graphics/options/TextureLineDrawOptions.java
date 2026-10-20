package dev.screwbox.core.graphics.options;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.ShaderSetup;


//TODO finish up
public record TextureLineDrawOptions(int drawOrder, double scale, ShaderSetup shader, Percent opacity) {
//TODO validation
    public static TextureLineDrawOptions scaled(final double scale) {
        return new TextureLineDrawOptions(0, scale, null, Percent.max());
    }

    public static TextureLineDrawOptions originalSize() {
        return scaled(1);
    }

    public TextureLineDrawOptions scale(final double scale) {
        return new TextureLineDrawOptions(drawOrder, scale, shader, opacity);
    }

    public TextureLineDrawOptions shader(final ShaderSetup shader) {
        return new TextureLineDrawOptions(drawOrder, scale, shader, opacity);
    }

    public TextureLineDrawOptions opacity(final Percent opacity) {
        return new TextureLineDrawOptions(drawOrder, scale, shader, opacity);
    }
}
