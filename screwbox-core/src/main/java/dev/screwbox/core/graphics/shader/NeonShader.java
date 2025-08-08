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
        final var mask = ImageOperations.applyFilter(source, new MaskColorFilter(maskColor));

        final var firstOutlineFilter = new OutlineImageFilter(Frame.fromImage(mask), neonColor.opacity(0.4));
        final var firstOutline = ImageOperations.applyFilter(mask, firstOutlineFilter);

        final var secondOutlineFilter = new OutlineImageFilter(Frame.fromImage(firstOutline), neonColor.opacity(0.1));
        final var secondOutline = ImageOperations.applyFilter(firstOutline, secondOutlineFilter);

        final var blurred = new BlurImageFilter(3).apply(secondOutline);
        return ImageOperations.stackImages(source, blurred);
    }
}
