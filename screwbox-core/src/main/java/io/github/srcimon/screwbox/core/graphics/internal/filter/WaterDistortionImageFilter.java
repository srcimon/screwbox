package io.github.srcimon.screwbox.core.graphics.internal.filter;

import io.github.srcimon.screwbox.core.graphics.Offset;

import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;
import java.util.Objects;

public class WaterDistortionImageFilter extends RGBImageFilter {

    private final BufferedImage source;
    private final WaterDistortionConfig config;

    public record WaterDistortionConfig(double seed, double amplitude, double frequencyX, double frequencyY,
                                        Offset offset) {

    }

    public WaterDistortionImageFilter(final BufferedImage source, final WaterDistortionConfig config) {
        this.source = Objects.requireNonNull(source, "source image must not be null");
        this.config = Objects.requireNonNull(config, "config must not be null");
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final double sourceX = x + Math.sin(config.seed + (x + config.offset.x()) * config.frequencyX + (config.offset.y() + y) * config.frequencyY) * config.amplitude;
        return source.getRGB((int) (Math.clamp(sourceX, 0, source.getWidth() - 1.0)), y);
    }
}
