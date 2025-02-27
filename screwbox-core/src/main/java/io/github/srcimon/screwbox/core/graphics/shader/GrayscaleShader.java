package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;

import javax.swing.*;
import java.awt.*;

public class GrayscaleShader implements Shader {

    @Override
    public Image applyOn(final Image image, final Percent progress) {
        return ImageUtil.applyFilter(image, new GrayFilter(false, 0));
    }

    @Override
    public boolean isAnimated() {
        return false;
    }

    @Override
    public String cacheKey() {
        return "grayscale";
    }
}
