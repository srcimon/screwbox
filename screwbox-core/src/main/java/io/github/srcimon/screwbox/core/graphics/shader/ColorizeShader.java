package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.AwtMapper;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import io.github.srcimon.screwbox.core.graphics.internal.filter.ColorizeImageFilter;

import java.awt.*;

public class ColorizeShader implements Shader {

    private final java.awt.Color startColor;
    private final java.awt.Color compareColor;
    private final java.awt.Color stopColor;

    public ColorizeShader(Color stop) {
        this(Color.TRANSPARENT, stop);
    }

    public ColorizeShader(Color start, Color stop) {
        this(Color.TRANSPARENT, start, stop);
    }

    public ColorizeShader(Color baseline, Color from, Color to) {
        this.startColor = AwtMapper.toAwtColor(from);
        this.compareColor = AwtMapper.toAwtColor(baseline);
        this.stopColor = AwtMapper.toAwtColor(to);
    }

    @Override
    public Image applyOn(final Image source, final Percent progress) {
        final int deltaRed = (int) (progress.value() * startColor.getRed() + progress.invert().value() * stopColor.getRed()) - compareColor.getRed();
        final int deltaGreen = (int) (progress.value() * startColor.getGreen() + progress.invert().value() * stopColor.getGreen()) - compareColor.getGreen();
        final int deltaBlue = (int) (progress.value() * startColor.getBlue() + progress.invert().value() * stopColor.getBlue()) - compareColor.getBlue();
        return ImageUtil.applyFilter(source, new ColorizeImageFilter(deltaRed, deltaGreen, deltaBlue));
    }

    @Override
    public String cacheKey() {
        return "ColorizeShader-%s-%s".formatted(startColor.getRGB(), stopColor.getRGB());
    }

    @Override
    public boolean isAnimated() {
        return true;
    }
}
