package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import io.github.srcimon.screwbox.core.graphics.internal.filter.WaterDistortionImageFilter;

import java.awt.*;

/**
 * Creates an animated water distortion effect on the image.
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

    public WaterDistortionShader(final int amplitude, final double frequency) {
        super("WaterDistortionShader-%s-%s".formatted(amplitude, frequency));
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var sourceImage = ImageUtil.toBufferedImage(source);
        final var filter = new WaterDistortionImageFilter(sourceImage, progress.value() * Math.PI * 2, amplitude, frequency);
        return ImageUtil.applyFilter(source, filter);
    }
}
