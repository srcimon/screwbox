package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;

import javax.swing.*;
import java.awt.*;

public class GrayscaleShader implements Shader {

    //TODO make unanimated

    @Override
    public Image applyOn(final Image image, final Percent progress) {
        return ImageUtil.applyFilter(image, new GrayFilter(true, (int) (progress.value() * 100)));
    }

    @Override
    public String key(Percent progress) {
        final int key = (int) (progress.value() * 100) / 4;
        return "grayscale-" + key;
    }
}
