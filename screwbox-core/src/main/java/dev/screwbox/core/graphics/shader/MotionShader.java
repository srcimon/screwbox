package dev.screwbox.core.graphics.shader;

import dev.screwbox.core.Percent;
import dev.screwbox.core.graphics.Shader;
import dev.screwbox.core.graphics.internal.ImageOperations;

import java.awt.*;
import java.awt.image.RGBImageFilter;
import java.io.Serial;

import static dev.screwbox.core.graphics.internal.ImageOperations.toBufferedImage;

public class MotionShader extends Shader {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int speedX;
    private final int speedY;

    public MotionShader(final int speedX, final int speedY) {
        super("MotionShader-%s-%s".formatted(speedX, speedY));
        this.speedX = speedX;
        this.speedY = speedY;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final int width = source.getWidth(null);
        final int height = source.getHeight(null);
        final int relocateX = getRelocateX(progress, speedX, width);
        final int relocateY = getRelocateX(progress, speedY, height);
        final var sourceImage = toBufferedImage(source);

        return ImageOperations.applyFilter(source, new RGBImageFilter() {
            @Override
            public int filterRGB(final int x, final int y, final int rgb) {
                return sourceImage.getRGB((relocateX + x) % width, (relocateY + y) % height);
            }
        });
    }

    private int getRelocateX(Percent progress, int speed, int size) {
        final int value = (int) (progress.value() * -speed * size);
        return value < 0 ? value + size * speed : value;
    }
}
