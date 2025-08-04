package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.ShaderSetup;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.internal.filter.BlurImageFilter;
import dev.screwbox.core.graphics.internal.filter.OutlineImageFilter;

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
                return brightness > threshold ? Color.WHITE.rgb() : 0;
            }
        };

        var result = ImageOperations.applyFilter(source, filter);
        var result2 = ImageOperations.applyFilter(result, new OutlineImageFilter(Frame.fromImage(result), Color.WHITE));
        var blurred = new BlurImageFilter(6).apply(result2);
Frame.fromImage(blurred).exportPng("skeleton.png");
        return ImageOperations.stack(source, blurred);
    }

    public static void main(String[] args) {
        ShaderSetup.combinedShader(new SizeIncreaseShader(4), new GlowShader(Percent.of(0.75))).createPreview(SpriteBundle.BOX.get().singleImage()).exportGif("shader.png");
    }
}
