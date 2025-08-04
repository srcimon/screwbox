package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.RGBImageFilter;
//TODO Add NoArgsConstructor to Shader
//TODO normalize all Shader CacheKeys
//TODO delete main

public class GlowShader extends Shader {

    private final int threshold;

    public GlowShader(final Percent threshold) {
        super("GlowShader-" + threshold.value(), false);
        this.threshold = threshold.rangeValue(0, 255);
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        var filter = new RGBImageFilter() {

            @Override
            public int filterRGB(int x, int y, int rgb) {
                var brightness = Color.rgb(rgb).brightness();
                return brightness > threshold ? rgb : 0;
            }
        };

        var result = ImageOperations.applyFilter(source, filter);
        return result;
    }

    public static void main(String[] args) {
        ShaderSetup.shader(new GlowShader(Percent.of(0.75))).createPreview(SpriteBundle.ACHIEVEMENT.get().singleImage()).exportGif("shader.png");
    }
}
