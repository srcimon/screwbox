package dev.screwbox.core.graphics.internal.filter;

import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.utils.MathUtil;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.RGBImageFilter;
import java.util.Objects;

public class DistortionImageFilter extends RGBImageFilter {

    private final BufferedImage source;
    private final DistortionConfig config;

    public record DistortionConfig(double seed, double amplitude, double frequencyX, double frequencyY,
                                   Offset offset) {

    }

    public DistortionImageFilter(final BufferedImage source, final DistortionConfig config) {
        this.source = Objects.requireNonNull(source, "source image must not be null");
        this.config = Objects.requireNonNull(config, "config must not be null");
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final double sourceX = x + MathUtil.fastSin(config.seed + (x + config.offset.x()) * config.frequencyX + (config.offset.y() + y) * config.frequencyY) * config.amplitude;
        return source.getRGB((int) (Math.clamp(sourceX, 0, source.getWidth() - 1.0)), y);
    }

    public void apply(final BufferedImage image) {
        var bytes = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        int[] out = new int[bytes.length];
        int height = image.getHeight();
        int width = image.getWidth();
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                final double sourceX = x + MathUtil.fastSin(config.seed + (x + config.offset.x()) * config.frequencyX + (config.offset.y() + y) * config.frequencyY) * config.amplitude;
                final int fixed = (int) (Math.clamp(sourceX, 0, width - 1.0));
                out[y *width+ x] = bytes[y*width + fixed];
            }
        }
        System.arraycopy(out, 0, bytes, 0, out.length);
    }
}
