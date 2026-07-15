package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.ai.BoidComponent;
import dev.screwbox.core.environment.ai.BoidObstacleComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.log.Log;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

public class PlaygroundApp {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private static BufferedImage image;
    private static int[] pixels;

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        // 2. Extract the direct underlying pixel memory bank
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
                .addSystem(e -> {
                    e.graphics().canvas().drawSprite(Sprite.fromImage(image), Offset.origin(), SpriteDrawOptions.originalSize());
                    updateAndDrawPixels((int)e.loop().frameNumber());
                });


        screwBox.start();
    }

    private static void updateAndDrawPixels(int frame) {
        // 4. BLAST changes directly to the 1D pixel array (1000x1000 = 1,000,000 operations)
        for (int y = 0; y < HEIGHT; y++) {
            int rowOffset = y * WIDTH;
            for (int x = 0; x < WIDTH; x++) {

                // Example dynamic pattern: Animated X/Y color waves
                int r = (x + frame) & 0xFF;
                int g = (y - frame) & 0xFF;
                int b = (x * y + frame) & 0xFF;

                // Pack RGB into a single hex integer (0xRRGGBB)
                pixels[rowOffset + x] = (r << 16) | (g << 8) | b;
            }
        }
    }
}