package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.internal.filter.ColorizeImageFilter;

import java.awt.*;
import java.io.Serial;

import static dev.screwbox.core.graphics.internal.AwtMapper.toAwtColor;

/**
 * Changes the {@link java.awt.Color} of all pixels.
 *
 * @since 2.15.0
 */
public class ColorizeShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

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
        final int deltaRed = Color.clampRgbRange((int) (invertValue * startColor.getRed() + progressValue * stopColor.getRed() - baseLineColor.getRed()));
        final int deltaGreen = Color.clampRgbRange((int) (invertValue * startColor.getGreen() + progressValue * stopColor.getGreen() - baseLineColor.getGreen()));
        final int deltaBlue = Color.clampRgbRange((int) (invertValue * startColor.getBlue() + progressValue * stopColor.getBlue() - baseLineColor.getBlue()));
        return ImageOperations.applyFilter(source, new ColorizeImageFilter(deltaRed, deltaGreen, deltaBlue));
    }

}
