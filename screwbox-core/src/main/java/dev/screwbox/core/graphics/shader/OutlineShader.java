package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Frame;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.internal.filter.OutlineImageFilter;

import java.awt.*;
import java.io.Serial;

/**
 * Adds an outline to the non transparent pixels within the image.
 *
 * @since 2.15.0
 */
public class OutlineShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Color color;
    private final boolean animateOpacity;

    /**
     * Creates a new instance without animation.
     */
    public OutlineShader(final Color color) {
        this(color, false);
    }

    /**
     * Creates a new instance.
     */
    public OutlineShader(final Color color, final boolean animateOpacity) {
        super("outline-" + color.hex(), animateOpacity);
        this.color = color;
        this.animateOpacity = animateOpacity;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var appliedColor = animateOpacity ? color.opacity(progress) : color;
        return ImageOperations.applyFilter(source, new OutlineImageFilter(Frame.fromImage(source), appliedColor));
    }
}
