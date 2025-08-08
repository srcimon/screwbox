package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.ShaderBundle;
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

    private final Color maskColor;
    private final Color neonColor;

    //TODO!!!!!!!!! COLOR RANGE SHADER -> LikeIntRangeShaderForCOlors
    public NeonShader(final Color maskColor, final Color neonColor) {
        super("NeonShader-" + maskColor.rgb() + "-" + neonColor.rgb(), false);
        this.maskColor = maskColor;
        this.neonColor = neonColor;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final int maskColorRgb = maskColor.rgb();
        var filter = new RGBImageFilter() {

            @Override
            public int filterRGB(int x, int y, int rgb) {
                return rgb == maskColorRgb ? maskColorRgb : 0;
            }
        };

        var result = ImageOperations.applyFilter(source, filter);
        var result2 = ImageOperations.applyFilter(result, new OutlineImageFilter(Frame.fromImage(result), neonColor.opacity(0.4)));
        var result3 = ImageOperations.applyFilter(result2, new OutlineImageFilter(Frame.fromImage(result2), neonColor.opacity(0.1)));
        var blurred = new BlurImageFilter(3).apply(result3);
        Image stack = ImageOperations.stack(source, blurred);
        return stack;
    }

    public static void main(String[] args) {
        ShaderBundle.NEON.get().createPreview(SpriteBundle.BOX.get().singleImage()).scaled(2).exportGif("neon.gif");
    }
}
