package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.ImageOperations;
import io.github.srcimon.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.image.BufferedImage;

//TODO document
//TODO changelog
//TODO test
//TODO github issue
public class PixelateShader extends Shader {

    private final int pixelSize;

    public PixelateShader(final int pixelSize) {
        super("PixelateShader-" + pixelSize, false);
        Validate.range(pixelSize, 2, 32, "pixel size must be in range from 2 to 32");
        this.pixelSize = pixelSize;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        final var sourceImage = ImageOperations.toBufferedImage(source);

        final var xSteps = sourceImage.getWidth() % pixelSize;
        final var ySteps = sourceImage.getHeight() % pixelSize;

        final var updatedImage = ImageOperations.cloneEmpty(source);
        final var graphics = (Graphics2D) updatedImage.getGraphics();

        for (int x = 0; x < xSteps; x += pixelSize) {
            for (int y = 0; y < ySteps; y += pixelSize) {
                graphics.setColor(new Color(getRgbInRange(x, y, sourceImage)));
                graphics.drawRect(x, y, pixelSize, pixelSize);
            }
        }
        return updatedImage;
    }

    private int getRgbInRange(int xP, int yP, BufferedImage sourceImage) {
        int r = 0;
        int g = 0;
        int b = 0;
        int a = 0;
        for (int x = 0; xP < pixelSize; x++) {
            for (int y = 0; yP < pixelSize; y++) {
                var colorAt = io.github.srcimon.screwbox.core.graphics.Color.rgb(sourceImage.getRGB(x, y));
                r += colorAt.r();
                g += colorAt.r();
                a += colorAt.alpha();
            }
        }
        int count = pixelSize * pixelSize;
        return io.github.srcimon.screwbox.core.graphics.Color.rgb(r / count, g / count, b / count, Percent.of(a / count / 255.0)).rgb();
    }
}