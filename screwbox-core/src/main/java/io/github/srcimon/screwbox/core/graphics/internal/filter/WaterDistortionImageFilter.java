package io.github.srcimon.screwbox.core.graphics.internal.filter;

import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;
import java.util.Objects;

public class WaterDistortionImageFilter extends RGBImageFilter {

    private final BufferedImage source;
    private final double seed;
    private final double amplitude;
    private final double frequenzy;

    public WaterDistortionImageFilter(final BufferedImage source, final double seed) {
        this(source, seed, 2.0, 0.5);
    }

    public WaterDistortionImageFilter(final BufferedImage source, final double seed, final double amplitude, final double frequenzy) {
        this.source = Objects.requireNonNull(source, "source image must not be null");
        this.seed = seed;
        this.amplitude = amplitude;
        this.frequenzy = frequenzy;
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final double sourceX = x + Math.sin(seed + y * frequenzy) * amplitude;
        return sourceX < 0 || sourceX > source.getWidth()
                ? 0 // transparent
                : source.getRGB((int) sourceX, y);
    }
}
