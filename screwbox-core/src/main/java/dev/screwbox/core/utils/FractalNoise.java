package dev.screwbox.core.utils;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.internal.ImageOperations;

import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;
import java.util.Objects;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * A fractal noise generator.
 *
 * @since 3.4.0
 */
public final class FractalNoise {

    private FractalNoise() {
    }

    /**
     * Creates a fractal noise value for the specified parameters. Will result in same value when called with same
     * values. Will create a visible border when near x / y zero.
     *
     * @since 3.4.0
     */
    public static Percent generateFractalNoise(final double zoom, final long seed, final Offset offset) {
        double value = 0.0;
        for (double gridZoom = zoom; gridZoom >= 1; gridZoom /= 2.0) {
            value += PerlinNoise.generatePerlinNoise(seed, offset.x() / gridZoom, offset.y() / gridZoom) * gridZoom;
        }
        return Percent.of((value / zoom + 1) / 2.0);
    }

    /**
     * Creates a fractal noise value for the specified parameters. Will result in same value when called with same
     * values. Will create a visible border when near x / y / z zero.
     *
     * @since 3.6.0
     */
    public static Percent generateFractalNoise3d(final double zoom, final long seed, final double x, final  double y, final double z) {
        double value = 0.0;
        for (double gridZoom = zoom; gridZoom >= 1; gridZoom /= 2.0) {
            value += PerlinNoise.generatePerlinNoise3d(seed, x / gridZoom, y / gridZoom, z / gridZoom) * gridZoom;
        }
        return Percent.of((value / zoom + 1) / 2.0);
    }

    /**
     * Creates a preview image using the specified input parameters.
     */
    public static Frame createPreview(final Color color, final Size size, double zoom, long seed) {
        Objects.requireNonNull(color, "color must not be null");
        Validate.isTrue(size::isValid, "invalid size for preview image");
        Validate.positive(zoom, "zoom must be positive");

        final var emptyImage = new BufferedImage(size.width(), size.height(), TYPE_INT_ARGB);
        final var previewImage = ImageOperations.applyFilter(emptyImage, new RGBImageFilter() {
            @Override
            public int filterRGB(final int x, final int y, final int rgb) {
                final var noise = generateFractalNoise(zoom, seed, Offset.at(x, y));
                return color.opacity(noise).rgb();
            }
        });
        return Frame.fromImage(previewImage);
    }
}
