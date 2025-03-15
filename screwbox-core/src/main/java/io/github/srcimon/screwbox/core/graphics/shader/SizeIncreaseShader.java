package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.AwtMapper;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Increases the size of the image.
 *
 * @since 2.15.0
 */
public class SizeIncreaseShader extends Shader {

    private final int increaseX;
    private final int increaseY;

    /**
     * Creates a new instance using the specified size increase.
     *
     * @param increase size increase (min 1 to max 32)
     *
     * @since 2.15.0
     */
    public SizeIncreaseShader(final int increase) {
        this(increase, increase);
    }

    /**
     * Creates a new instance using the specified size increase.
     *
     * @param increaseX size increase x (min 0 to max 32)
     * @param increaseY size increase y (min 0 to max 32)
     *
     * @since 2.17.0
     */
    public SizeIncreaseShader(final int increaseX, final int increaseY) {
        super("size-increase-%s-%s".formatted(increaseX, increaseY), false);
        Validate.range(increaseX, 0, 32, "only size increase from 1 to 32 is supported");
        Validate.range(increaseY, 0, 32, "only size increase from 1 to 32 is supported");
        Validate.isTrue(() -> increaseX != 0 || increaseY != 0, "at least one axis should be size increased");
        this.increaseX = increaseX;
        this.increaseY = increaseY;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final int resultWidth = source.getWidth(null) + increaseX * 2;
        final int resultHeight = source.getHeight(null) + increaseY * 2;
        final var newImage = new BufferedImage(resultWidth, resultHeight, BufferedImage.TYPE_INT_ARGB);
        final var graphics = newImage.getGraphics();
        graphics.drawImage(source, increaseX, increaseY, null);
        graphics.dispose();
        return newImage;
    }
}
