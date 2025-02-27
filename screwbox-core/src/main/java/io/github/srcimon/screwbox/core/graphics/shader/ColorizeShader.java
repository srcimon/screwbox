package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;

import java.awt.*;
import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;

public class ColorizeShader implements Shader {


    @Override
    public Image applyOn(Image image, Percent progress) {

        int redIncrement = (int) (progress.value() * 40);
        int greenIncrement = (int) (progress.value() * -20);
        int blueIncrement = (int) (progress.value() * -40);

        ImageFilter filter = new RGBImageFilter() {
            @Override
            public int filterRGB(final int x, final int y, final int rgb) {
                int alpha = (rgb & 0xff000000);
                int red = (rgb & 0xff0000) >> 16;
                int green = (rgb & 0x00ff00) >> 8;
                int blue = (rgb & 0x0000ff);

                red = Math.max(0, Math.min(0xff, red + redIncrement));
                green = Math.max(0, Math.min(0xff, green + greenIncrement));
                blue = Math.max(0, Math.min(0xff, blue + blueIncrement));

                return alpha | (red << 16) | (green << 8) | blue;
            }
        };
        return ImageUtil.applyFilter(image, filter);
    }

    @Override
    public boolean isAnimated() {
        return true;
    }
}
