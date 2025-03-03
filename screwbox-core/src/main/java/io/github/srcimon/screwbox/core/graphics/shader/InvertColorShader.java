package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;
import io.github.srcimon.screwbox.core.graphics.internal.filter.InvertColorFilter;

import java.awt.*;

/**
 * Inverts all colors of the image.
 *
 * @since 2.15.0
 */
public class InvertColorShader extends Shader {

    public InvertColorShader() {
        super("invert-colors", false);
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        return ImageOperations.applyFilter(source, new InvertColorFilter());
    }
}
