package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.ShaderBundle;
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

        var result2 = ImageOperations.applyFilter(result, new OutlineImageFilter(Frame.fromImage(result), color.opacity(0.4)));
        var result3 = ImageOperations.applyFilter(result2, new OutlineImageFilter(Frame.fromImage(result2), color.opacity(0.1)));
        var blurred = new BlurImageFilter(3).apply(result3);


      //  var x = ImageOperations.applyFilter(blurred, brightenFilter);
        return ImageOperations.stack(source, blurred);
    }

    public static void main(String[] args) {
        ShaderBundle.NEON.get().createPreview(SpriteBundle.BOX.get().singleImage()).scaled(2).exportGif("neon.gif");
    }
}
