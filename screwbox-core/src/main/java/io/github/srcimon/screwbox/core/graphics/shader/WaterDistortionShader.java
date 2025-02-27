package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import io.github.srcimon.screwbox.core.graphics.internal.filter.WaterDistortionImageFilter;

import java.awt.*;
import java.awt.image.BufferedImage;
//TODO kind of duplicated to WaterDistortionImageFilter
public class WaterDistortionShader implements Shader {

    @Override
    public Image applyOn(Image image, Percent progress) {
        BufferedImage sourceImage = ImageUtil.toBufferedImage(image);
        return ImageUtil.applyFilter(image, new WaterDistortionImageFilter(sourceImage, progress.value(), 4, Math.PI));
    }

    @Override
    public String key(Percent progress) {
        final int key = (int) (progress.value() * 100.0);//TODO ugly and always the same!!!
        System.out.println(key);
        return "water-distortion-"+ key;
    }
}
