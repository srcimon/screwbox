package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.internal.filter.BlurImageFilter;
import dev.screwbox.core.graphics.internal.filter.MaskColorFilter;
import dev.screwbox.core.graphics.internal.filter.OutlineImageFilter;

import java.awt.*;
//TODO Add NoArgsConstructor to Shader
//TODO normalize all Shader CacheKeys

/**
 * Adds a neon glow effect to the specified {@link Color}. {@link Color} of glow can also be specified.
 *
 * @since 3.7.0
 */
public class NeonShader extends Shader {

    private final Color maskColor;
    private final Color neonColor;

    public NeonShader(final Color maskColor, final Color neonColor) {
        super("NeonShader-" + maskColor.rgb() + "-" + neonColor.rgb(), false);
        this.maskColor = maskColor;
        this.neonColor = neonColor;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        var result = ImageOperations.applyFilter(source, new MaskColorFilter(maskColor));
        var result2 = ImageOperations.applyFilter(result, new OutlineImageFilter(Frame.fromImage(result), neonColor.opacity(0.4)));
        var result3 = ImageOperations.applyFilter(result2, new OutlineImageFilter(Frame.fromImage(result2), neonColor.opacity(0.1)));
        var blurred = new BlurImageFilter(3).apply(result3);
        return ImageOperations.stack(source, blurred);
    }
}
