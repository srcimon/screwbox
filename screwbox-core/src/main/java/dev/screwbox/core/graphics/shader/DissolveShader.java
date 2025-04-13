package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.internal.filter.MaskImageFilter;

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
