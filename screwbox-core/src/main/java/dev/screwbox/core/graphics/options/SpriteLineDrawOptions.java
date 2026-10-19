package dev.screwbox.core.graphics.options;

import dev.screwbox.core.graphics.ShaderSetup;

//TODO rename to SpriteStrokeDrawOptions
//TODO finish up
public record SpriteLineDrawOptions(int drawOrder, double scale, ShaderSetup shader) {//TODO shader
    //TODO scale

    public SpriteLineDrawOptions scale(final double scale) {
        return new SpriteLineDrawOptions(drawOrder, scale, shader);
    }

    public SpriteLineDrawOptions shader(final ShaderSetup shader) {
        return new SpriteLineDrawOptions(drawOrder, scale, shader);
    }
}
