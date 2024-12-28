package io.github.srcimon.screwbox.core.graphics.internal.filter;

import io.github.srcimon.screwbox.core.Time;

import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;

public class WaterImageFilter extends RGBImageFilter {

    private final BufferedImage input;
    private final double seed;

    public WaterImageFilter(final BufferedImage input) {
        this.input = input;
        this.seed =  Time.now().milliseconds() * 0.005;
    }

    @Override
    public int filterRGB(int x, int y, int rgb) {
        return input.getRGB((int) (Math.clamp(x + Math.sin(seed + y * 0.5) * 2.0, 0, input.getWidth() - 1)), y);
    }
}
