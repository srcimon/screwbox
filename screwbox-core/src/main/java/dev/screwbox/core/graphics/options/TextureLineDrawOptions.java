package dev.screwbox.core.graphics.options;

import dev.screwbox.core.graphics.ShaderSetup;

//TODO actually use drawOrder
//TODO SoftBodyRenderComponent.surfaceTexture
//TODO finish up
public record TextureLineDrawOptions(int drawOrder, double scale, ShaderSetup shader) {
    //TODO scale

    public static TextureLineDrawOptions scaled(final double scale) {
        return new TextureLineDrawOptions(0, scale, null);
    }

    public static TextureLineDrawOptions originalSize() {
        return scaled(1);
    }

    public TextureLineDrawOptions scale(final double scale) {
        return new TextureLineDrawOptions(drawOrder, scale, shader);
    }

    public TextureLineDrawOptions shader(final ShaderSetup shader) {
        return new TextureLineDrawOptions(drawOrder, scale, shader);
    }
}
