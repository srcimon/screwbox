package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Offset;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;
import io.github.srcimon.screwbox.core.graphics.internal.filter.DistortionImageFilter;

import java.awt.*;

import static io.github.srcimon.screwbox.core.graphics.internal.ImageOperations.toBufferedImage;

/**
 * Creates an distortion effect on the image e.g. used for water or wind effects.
 * For best results can be combined with {@link SizeIncreaseShader}.
 *
 * @since 2.15.0
 */
public class DistortionShader extends Shader {

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
