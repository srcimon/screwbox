package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.playground.render.GasRenderComponent;
import dev.screwbox.playground.render.GasRenderSystem;
import dev.screwbox.playground.simulate.GasSimulationComponent;
import dev.screwbox.playground.simulate.GasSimulationSystem;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class PlaygroundApp {

    private static final int WIDTH = GraphicsConfiguration.DEFAULT_RESOLUTION.width();
    private static final int HEIGHT = GraphicsConfiguration.DEFAULT_RESOLUTION.height();
    private static int[] pixels;

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        screwBox.loop().unlockFps();
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(new GasSimulationSystem())
            .addSystem(new GasRenderSystem())
                .addEntity(new Entity().bounds(Bounds.atPosition(32, 32, 64,64))
                    .add(new GasRenderComponent())
                    .add(new GasSimulationComponent(1)));
//            .addSystem(e -> {
//                e.graphics().canvas().drawSprite(Sprite.fromImage(image), Offset.origin(), SpriteDrawOptions.originalSize());
//                updateAndDrawPixels((int) e.loop().frameNumber());
//            });

        screwBox.start();
    }

    private static void updateAndDrawPixels(int frame) {
        Arrays.parallelSetAll(pixels, gid -> {
            int x = gid % WIDTH;
            int y = gid / WIDTH;

            int r = (x + frame) & 0xFF;
            int g = (y - frame) & 0xFF;
            int b = (x * y + frame) & 0xFF;

            return (r << 16) | (g << 8) | b;
        });
    }
}