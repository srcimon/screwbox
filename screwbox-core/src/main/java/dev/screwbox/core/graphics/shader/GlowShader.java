package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.SpriteBundle;

import java.awt.*;
//TODO Add NoArgsConstructor to Shader
//TODO normalize all Shader CacheKeys
//TODO delete main

public class GlowShader extends Shader {

    public GlowShader() {
        super("GlowShader", false);
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        return source;
    }

    public static void main(String[] args) {
        ShaderSetup.shader(new GlowShader()).createPreview(SpriteBundle.ACHIEVEMENT.get().singleImage()).exportGif("shader.png");
    }
}
