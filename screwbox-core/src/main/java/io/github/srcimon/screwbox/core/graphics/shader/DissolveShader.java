package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.SpriteBundle;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;
import io.github.srcimon.screwbox.core.graphics.internal.filter.MaskImageFilter;

import java.awt.*;
import java.io.Serial;

public class DissolveShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    public DissolveShader() {
        super("dissolve");
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var mask = SpriteBundle.CLOUDS.get().singleFrame();
        final int threshold = progress.rangeValue(0, 255);
        return ImageOperations.applyFilter(source, new MaskImageFilter(mask, threshold, false));
    }
}
