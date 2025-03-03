package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.awt.*;

/**
 * Increases the size of the image.
 *
 * @since 2.15.0
 */
public class SizeIncreaseShader extends Shader {

    private final int increase;

    /**
     * Creates a new instance using the specified size increase.
     *
     * @param increase size increase (min 1 to max 32)
     */
    public SizeIncreaseShader(final int increase) {
        super("size-increase-%s".formatted(increase), false);
        Validate.range(increase, 1, 32, "only size increase from 1 to 32 is supported");
        this.increase = increase;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        return ImageOperations.addBorder(source, increase, Color.TRANSPARENT);
    }
}
