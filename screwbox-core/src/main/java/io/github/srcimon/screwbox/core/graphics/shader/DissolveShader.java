package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;
import io.github.srcimon.screwbox.core.graphics.internal.filter.MaskImageFilter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DissolveShader extends Shader {

    private static final Frame mask = SpriteBundle.CLOUDS.get().singleFrame();

    public DissolveShader() {
        super("dissolve");
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        return ImageOperations.applyFilter(source, new MaskImageFilter(mask, (int) (progress.value() * 255)));
    }
}
