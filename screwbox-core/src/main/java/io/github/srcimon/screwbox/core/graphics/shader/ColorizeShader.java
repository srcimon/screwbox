package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import io.github.srcimon.screwbox.core.graphics.internal.filter.ColorizeImageFilter;

import java.awt.*;

import static io.github.srcimon.screwbox.core.graphics.internal.AwtMapper.toAwtColor;

/**
 * Changes the {@link java.awt.Color} of all pixels.
 *
 * @since 2.15.0
 */
public class ColorizeShader extends Shader {

    private final java.awt.Color startColor;
    private final java.awt.Color baseLineColor;
    private final java.awt.Color stopColor;

    /**
     * Creates a new instance without baseline and start color.
     */
    public ColorizeShader(final Color stop) {
        this(Color.TRANSPARENT, stop);
    }

    /**
     * Creates a new instance without baseline color.
     */
    public ColorizeShader(final Color start, final Color stop) {
        this(Color.TRANSPARENT, start, stop);
    }

    /**
     * Creates a new instance with baseline start and stop color.
     */
    public ColorizeShader(final Color baseline, final Color start, final Color stop) {
        super("ColorizeShader-%s-%s-%s".formatted(baseline.hex(), start.hex(), stop.hex()));
        this.baseLineColor = toAwtColor(baseline);
        this.startColor = toAwtColor(start);
        this.stopColor = toAwtColor(stop);
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final double progressValue = progress.value();
        final double invertValue = progress.invert().value();
        final int deltaRed = Math.clamp((int) (invertValue * startColor.getRed() + progressValue * stopColor.getRed()) - baseLineColor.getRed(), 0, 255);
        final int deltaGreen = Math.clamp((int) (invertValue * startColor.getGreen() + progressValue * stopColor.getGreen()) - baseLineColor.getGreen(), 0, 255);
        final int deltaBlue = Math.clamp((int) (invertValue * startColor.getBlue() + progressValue * stopColor.getBlue()) - baseLineColor.getBlue(), 0, 255);
        return ImageUtil.applyFilter(source, new ColorizeImageFilter(deltaRed, deltaGreen, deltaBlue));
    }
}
