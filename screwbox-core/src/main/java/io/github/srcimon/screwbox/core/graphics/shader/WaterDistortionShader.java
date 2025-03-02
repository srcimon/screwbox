package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Offset;
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
    private final double frequencyX;
    private final double frequencyY;

    /**
     * Creates an instance with default amplitude and frequency.
     */
    public WaterDistortionShader() {
        this(2, 0.5, 0.25);
    }

    /**
     * Creates an instance with custom amplitude and frequency.
     */
    public WaterDistortionShader(final int amplitude, final double frequencyX, final double frequencyY) {
        super("WaterDistortionShader-%s-%s-%s".formatted(amplitude, frequencyX, frequencyY));
        this.amplitude = amplitude;
        this.frequencyX = frequencyX;
        this.frequencyY = frequencyY;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var sourceImage = toBufferedImage(source);
        final double seed = progress.value() * Math.PI * 2;
        final var filterConfig = new WaterDistortionImageFilter.WaterDistortionConfig(
                seed, amplitude, frequencyX, frequencyY, Offset.origin());
        final var filter = new WaterDistortionImageFilter(sourceImage, filterConfig);
        return ImageUtil.applyFilter(sourceImage, filter);
    }
}
