package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;
import io.github.srcimon.screwbox.core.graphics.internal.filter.MaskImageFilter;

import java.awt.*;

public class DissolveShader extends Shader {

    public DissolveShader() {
        super("dissolve");
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var mask = SpriteBundle.CLOUDS.get().singleFrame();
        final int threshold = (int) (progress.value() * 255);
        return ImageOperations.applyFilter(source, new MaskImageFilter(mask, threshold));
    }
}
