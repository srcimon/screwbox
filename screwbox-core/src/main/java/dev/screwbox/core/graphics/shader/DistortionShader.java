package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.internal.filter.DistortionImageFilter;

import java.awt.*;
import java.io.Serial;

import static dev.screwbox.core.graphics.internal.ImageOperations.toBufferedImage;

/**
 * Creates an distortion effect on the image e.g. used for water or wind effects.
 * For best results can be combined with {@link SizeIncreaseShader}.
 *
 * @since 2.15.0
 */
public class DistortionShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int amplitude;
    private final double frequencyX;
    private final double frequencyY;

    /**
     * Creates an instance with default amplitude and frequency.
     */
    public DistortionShader() {
        this(2, 0.5, 0.25);
    }

    /**
     * Creates an instance with custom amplitude and frequency.
     */
    public DistortionShader(final int amplitude, final double frequencyX, final double frequencyY) {
        super("DistortionShader-%s-%s-%s".formatted(amplitude, frequencyX, frequencyY));
        this.amplitude = amplitude;
        this.frequencyX = frequencyX;
        this.frequencyY = frequencyY;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var sourceImage = toBufferedImage(source);
        final double seed = progress.value() * Math.PI * 2;
        final var filterConfig = new DistortionImageFilter.DistortionConfig(
                seed, amplitude, frequencyX, frequencyY, Offset.origin());
        final var filter = new DistortionImageFilter(sourceImage, filterConfig);
        return ImageOperations.applyFilter(sourceImage, filter);
    }
}
