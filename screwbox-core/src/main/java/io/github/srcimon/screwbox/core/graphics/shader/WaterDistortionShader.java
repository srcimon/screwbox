package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import io.github.srcimon.screwbox.core.graphics.internal.filter.WaterDistortionImageFilter;

import java.awt.*;

import static io.github.srcimon.screwbox.core.graphics.internal.ImageUtil.toBufferedImage;

/**
 * Creates an animated water distortion effect on the image.
 * For best results can be combined with {@link SizeIncreaseShader}.
 *
 * @since 2.15.0
 */
public class WaterDistortionShader extends Shader {

    private final int amplitude;
    private final double frequency;

    /**
     * Creates an instance with default amplitude and frequency.
     */
    public WaterDistortionShader() {
        this(2, 0.5);
    }

    /**
     * Creates an instance with custom amplitude and frequency.
     */
    public WaterDistortionShader(final int amplitude, final double frequency) {
        super("WaterDistortionShader-%s-%s-".formatted(amplitude, frequency));
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var sourceImage = toBufferedImage(source);
        final double seed = progress.value() * Math.PI * 2;
        final var filter = new WaterDistortionImageFilter(sourceImage, seed, amplitude, frequency);
        return ImageUtil.applyFilter(sourceImage, filter);
    }
}
