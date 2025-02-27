package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import io.github.srcimon.screwbox.core.graphics.internal.filter.WaterDistortionImageFilter;

import java.awt.*;

public class WaterDistortionShader implements Shader {

    private final int amplitude;
    private final double frequenzy;
    private final String cacheKey;

    public WaterDistortionShader() {
        this(2, 0.5);
    }

    public WaterDistortionShader(int amplitude, double frequenzy) {
        this.amplitude = amplitude;
        this.frequenzy = frequenzy;
        this.cacheKey = "WaterDistortionShader-%s-%s".formatted(amplitude, frequenzy);
    }

    @Override
    public Image applyOn(Image image, Percent progress) {
        final var sourceImage = ImageUtil.toBufferedImage(image);
        final var filter = new WaterDistortionImageFilter(sourceImage, progress.value() * Math.PI * 2, amplitude, frequenzy);
        return ImageUtil.applyFilter(image, filter);
    }

    @Override
    public boolean isAnimated() {
        return true;
    }

    @Override
    public String cacheKey() {
        return cacheKey;
    }
}
