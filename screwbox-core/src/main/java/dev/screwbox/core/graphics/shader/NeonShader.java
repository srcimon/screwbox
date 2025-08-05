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
import java.awt.image.RGBImageFilter;
//TODO Add NoArgsConstructor to Shader
//TODO normalize all Shader CacheKeys
//TODO delete main

public class NeonShader extends Shader {

    private final int threshold;
    private final Color color;

    public NeonShader(final Percent threshold, final Color color) {
        super("GlowShader-" + threshold.value() + "-"+ color.rgb(), false);
        this.threshold = threshold.rangeValue(0, 255);
        this.color = color;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        var filter = new RGBImageFilter() {

            @Override
            public int filterRGB(int x, int y, int rgb) {
                var brightness = Color.rgb(rgb).brightness();
                return brightness > threshold ? color.rgb() : 0;
            }
        };

        var result = ImageOperations.applyFilter(source, filter);
        var result2 = ImageOperations.applyFilter(result, new OutlineImageFilter(Frame.fromImage(result), color.opacity(0.2)));
        var blurred = new BlurImageFilter(3).apply(result2);
        var brigtimage = Frame.fromImage(blurred);

        var brightenFilter = new RGBImageFilter() {

            @Override
            public int filterRGB(int x, int y, int rgb) {
                var inColor = Color.rgb(rgb);
                var lightness = brigtimage.colorAt(x, y).opacity();
                return inColor.brighten(Percent.of(lightness.value())).rgb();
            }
        };
        var x = ImageOperations.applyFilter(blurred, brightenFilter);
        var x2 = ImageOperations.stack(x, result);
        return ImageOperations.stack(source, x2);
    }

    public static void main(String[] args) {
        ShaderSetup.combinedShader(new SizeIncreaseShader(4), new NeonShader(Percent.of(0.75), Color.RED)).createPreview(SpriteBundle.ACHIEVEMENT.get().singleImage()).exportGif("shader.png");
    }

}
