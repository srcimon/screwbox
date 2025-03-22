package io.github.srcimon.screwbox.core.graphics.shader;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.graphics.Shader;
import io.github.srcimon.screwbox.core.graphics.internal.AwtMapper;
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
        Validate.range(pixelSize, 1, 32, "pixel size must be in range from 1 to 32");
        this.pixelSize = pixelSize;
    }

    @Override
    public Image apply(final Image source, final Percent progress) {
        if(pixelSize == 1) {
            return source;
        }
        final var sourceImage = ImageOperations.toBufferedImage(source);

        final var updatedImage = ImageOperations.cloneEmpty(source);
        final var graphics = (Graphics2D) updatedImage.getGraphics();

        for (int x = 0; x < sourceImage.getWidth(); x += pixelSize) {
            for (int y = 0; y < sourceImage.getHeight(); y += pixelSize) {
                graphics.setColor(AwtMapper.toAwtColor(io.github.srcimon.screwbox.core.graphics.Color.rgb(getRgbInRange(x, y, sourceImage))));
                graphics.fillRect(x, y, pixelSize, pixelSize);
            }
        }
        graphics.dispose();
        return updatedImage;
    }

    private int getRgbInRange(int xP, int yP, BufferedImage sourceImage) {
        int r = 0;
        int g = 0;
        int b = 0;
        double opacity = 0;
        int count=0;
        int maxX = Math.min(pixelSize + xP, sourceImage.getWidth());
        int maxY = Math.min(pixelSize + yP, sourceImage.getHeight());
        for (int x = xP; x < maxX; x++) {
            for (int y = yP; y < maxY; y++) {
                var colorAt = io.github.srcimon.screwbox.core.graphics.Color.rgb(sourceImage.getRGB(x, y));
                r += colorAt.r();
                g += colorAt.g();
                b += colorAt.b();
                opacity += colorAt.opacity().value();
                count++;
            }
        }

        return io.github.srcimon.screwbox.core.graphics.Color.rgb(r / count, g / count, b / count, Percent.of(opacity / count)).rgb();
    }
}