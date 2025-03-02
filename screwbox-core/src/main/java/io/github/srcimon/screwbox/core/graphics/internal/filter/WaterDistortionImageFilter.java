package io.github.srcimon.screwbox.core.graphics.internal.filter;

import io.github.srcimon.screwbox.core.graphics.Offset;

import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;
import java.util.Objects;

public class WaterDistortionImageFilter extends RGBImageFilter {

    private final BufferedImage source;
    private final double seed;
    private final double amplitude;
    private final double frequency;
private Offset offset = Offset.at(0,0);
    public WaterDistortionImageFilter(final BufferedImage source, final double seed) {
        this(source, seed, 2.0, 0.5);
    }

    public WaterDistortionImageFilter(final BufferedImage source, final double seed, final double amplitude, final double frequency) {
        this.source = Objects.requireNonNull(source, "source image must not be null");
        this.seed = seed;
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final double sourceX = x + Math.sin(seed + (x+offset.x()) * 0.4 +  (offset.y() +y) * frequency) * amplitude;//TODO make x configurable
        return source.getRGB((int) (Math.clamp(sourceX, 0, source.getWidth() - 1.0)), y);
    }

    public void setOffset(Offset offset) {
        this.offset = offset;
    }
}
