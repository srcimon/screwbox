package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import io.github.srcimon.screwbox.core.graphics.internal.filter.WaterDistortionImageFilter;

import java.awt.*;

import static io.github.srcimon.screwbox.core.graphics.internal.ImageUtil.addBorder;
import static io.github.srcimon.screwbox.core.graphics.internal.ImageUtil.toBufferedImage;

/**
 * Creates an animated water distortion effect on the image.
 *
 * @since 2.15.0
 */
public class WaterDistortionShader extends Shader {

    private final int amplitude;
    private final double frequency;
    private final int sizeIncrease;

    /**
     * Creates an instance with default amplitude and frequency.
     */
    public WaterDistortionShader() {
        this(2, 0.5);
    }

    public WaterDistortionShader(final int amplitude, final double frequency) {
        this(amplitude, frequency, 0);
    }

    public WaterDistortionShader(final int amplitude, final double frequency, int sizeIncrease) {
        super("WaterDistortionShader-%s-%s-%s-".formatted(amplitude, frequency, sizeIncrease));
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.sizeIncrease = sizeIncrease;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var sourceImage = toBufferedImage(sizeIncrease > 0 ? addBorder(source, sizeIncrease, Color.TRANSPARENT) : source);
        final var filter = new WaterDistortionImageFilter(sourceImage, progress.value() * Math.PI * 2, amplitude, frequency);
        return ImageUtil.applyFilter(sourceImage, filter);
    }
}
