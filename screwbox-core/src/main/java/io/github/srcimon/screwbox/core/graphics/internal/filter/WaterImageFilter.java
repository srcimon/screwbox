package io.github.srcimon.screwbox.core.graphics.internal.filter;

import io.github.srcimon.screwbox.core.Time;

import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;

public class WaterImageFilter extends RGBImageFilter {

    private final BufferedImage input;
    private final double seed;

    private double speed = 0.005;
    private double amplitude = 2.0;
    private double frequenzy = 0.5;

    public WaterImageFilter(final BufferedImage input) {
        this.input = input;
        this.seed = Time.now().milliseconds() * speed;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        final double sourceX = x + Math.sin(seed + y * frequenzy) * amplitude;
        return input.getRGB((int) (Math.clamp(sourceX, 0, input.getWidth() - 1)), y);
    }
}
