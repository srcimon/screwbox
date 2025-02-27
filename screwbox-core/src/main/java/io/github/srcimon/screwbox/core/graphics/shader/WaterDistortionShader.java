package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import io.github.srcimon.screwbox.core.graphics.internal.filter.WaterDistortionImageFilter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class WaterDistortionShader implements Shader {

    private final int amplitude;
    private final double frequenzy;
    private final String baseKey;

    public WaterDistortionShader() {
        this(2, 0.5);
    }

    public WaterDistortionShader(int amplitude, double frequenzy) {
        this.amplitude = amplitude;
        this.frequenzy = frequenzy;
        this.baseKey = "water-distortion-" + amplitude + "-" + frequenzy + "-";
    }

    @Override
    public Image applyOn(Image image, Percent progress) {
        BufferedImage sourceImage = ImageUtil.toBufferedImage(image);
        return ImageUtil.applyFilter(image, new WaterDistortionImageFilter(sourceImage, progress.value() * Math.PI * 2, amplitude, frequenzy));
    }

    @Override
    public String key(Percent progress) {
        final int key = (int) (progress.value() * 100.0);//TODO ugly and always the same!!!
        return baseKey + key;
    }
}
