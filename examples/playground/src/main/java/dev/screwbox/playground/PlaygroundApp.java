package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Kernel;
import java.util.Arrays;

public class PlaygroundApp {

    private static final int WIDTH = GraphicsConfiguration.DEFAULT_RESOLUTION.width();
    private static final int HEIGHT = GraphicsConfiguration.DEFAULT_RESOLUTION.height();
    private static BufferedImage image;
    private static int[] pixels;

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        // 2. Extract the direct underlying pixel memory bank
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        screwBox.loop().unlockFps();
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(e -> {
                e.graphics().canvas().drawSprite(Sprite.fromImage(image), Offset.origin(), SpriteDrawOptions.originalSize());
                updateAndDrawPixels((int) e.loop().frameNumber());
            });

        screwBox.start();
    }

    private static void updateAndDrawPixels(int frame) {
        // Nutzt nativ alle CPU-Kerne parallel
        Arrays.parallelSetAll(pixels, gid -> {
            // Berechne X und Y aus dem 1D-Index (0 bis 999.999)
            int x = gid % WIDTH;
            int y = gid / WIDTH;

            int r = (x + frame) & 0xFF;
            int g = (y - frame) & 0xFF;
            int b = (x * y + frame) & 0xFF;

            // Packt RGB in das Pixel-Array
            return (r << 16) | (g << 8) | b;
        });
    }
}