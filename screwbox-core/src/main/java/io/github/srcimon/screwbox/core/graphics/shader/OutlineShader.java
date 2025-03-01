package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Frame;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageUtil;
import io.github.srcimon.screwbox.core.graphics.internal.filter.OutlineImageFilter;

import java.awt.*;

/**
 * Adds an outline to the non transparent pixels within the image.
 */
public class OutlineShader extends Shader {

    private Color color;

    public OutlineShader(final Color color) {
        super("outline-" + color.hex(), false);
        this.color = color;
    }

    @Override
    public Image apply(Image source, Percent progress) {
        return ImageUtil.applyFilter(source, new OutlineImageFilter(Frame.fromImage(source), color));
    }
}
