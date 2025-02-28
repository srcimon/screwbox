package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import io.github.srcimon.screwbox.core.graphics.internal.filter.GrayscaleFilter;

import java.awt.*;

/**
 * Converts image into grayscale image.
 *
 * @since 2.15.0
 */
public class GrayscaleShader implements Shader {

    public static final GrayscaleFilter GRAYSCALE_FILTER = new GrayscaleFilter();

    @Override
    public Image apply(final Image source, final Percent progress) {
        return ImageUtil.applyFilter(source, GRAYSCALE_FILTER);
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
