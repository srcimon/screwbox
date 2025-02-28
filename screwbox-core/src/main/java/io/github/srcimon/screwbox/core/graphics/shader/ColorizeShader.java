package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.AwtMapper;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import io.github.srcimon.screwbox.core.graphics.internal.filter.ColorizeImageFilter;

import java.awt.*;

/**
 * Changes the {@link java.awt.Color} of all pixels.
 */
public class ColorizeShader implements Shader {

    private final java.awt.Color startColor;
    private final java.awt.Color baseLineColor;
    private final java.awt.Color stopColor;
    private final String cacheKey;

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
        this.baseLineColor = AwtMapper.toAwtColor(baseline);
        this.startColor = AwtMapper.toAwtColor(start);
        this.stopColor = AwtMapper.toAwtColor(stop);
        this.cacheKey = "ColorizeShader-%s-%s-%s".formatted(baseLineColor.getRGB(), startColor.getRGB(), stopColor.getRGB());
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final double progressValue = progress.value();
        final double invertValue = progress.invert().value();
        final int deltaRed = (int) (invertValue * startColor.getRed() + progressValue * stopColor.getRed()) - baseLineColor.getRed();
        final int deltaGreen = (int) (invertValue * startColor.getGreen() + progressValue * stopColor.getGreen()) - baseLineColor.getGreen();
        final int deltaBlue = (int) (invertValue * startColor.getBlue() + progressValue * stopColor.getBlue()) - baseLineColor.getBlue();
        return ImageUtil.applyFilter(source, new ColorizeImageFilter(deltaRed, deltaGreen, deltaBlue));
    }

    @Override
    public String cacheKey() {
        return cacheKey;
    }

    @Override
    public boolean isAnimated() {
        return true;
    }
}
