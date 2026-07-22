package dev.screwbox.core.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.internal.ViewportManager;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.smoke.Smoke;
import dev.screwbox.core.loop.internal.Updatable;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class DefaultSmoke implements Smoke, Updatable {

//TODO support split screen
    private final ViewportManager viewportManager;
    private int cellSize = 8;
    private int screenBorder=-32;

    private Vector worldAnchor;
    private FluidSimulation simulation;

    public DefaultSmoke(final ViewportManager viewportManager) {
        this.viewportManager = viewportManager;
    }

    @Override
    public Smoke enable() {
        Bounds visibleArea = viewportManager.defaultViewport().visibleArea().expand(screenBorder);//TODO remove expand
        var boundsArea = visibleArea.snapExpand(cellSize);
        boundsArea = boundsArea.resize(
            Math.max(boundsArea.width(), boundsArea.height()),
            Math.max(boundsArea.width(), boundsArea.height()));
        Vector origin = boundsArea.origin();
        worldAnchor = origin;
        simulation = new FluidSimulation((int)(boundsArea.width() / cellSize));
        return this;
    }

    @Override
    public Smoke disable() {
        simulation = null;
        worldAnchor = null;
        return this;
    }

    @Override
    public void update() {
        if(simulation != null) {
            //TODO get delta from update()
            simulation.step(0.002 , 0.0004,0.0003, 4);
            simulation.addDensity(10,10, 2);
            simulation.addVelocity(10,10, 8,8);

            BufferedImage image = createImage();

            double scale = simulation.size() / viewportManager.defaultViewport().camera().zoom();
            Offset origin = viewportManager.defaultViewport().toCanvas(worldAnchor);

            viewportManager.defaultViewport().canvas().drawSprite(Sprite.fromImage(image), origin, SpriteDrawOptions
                .scaled(scale)
                .drawOrder(Order.DEBUG_OVERLAY.drawOrder()));//TODO size
            viewportManager.defaultViewport().canvas().drawRectangle(origin, Size.of(100,100), RectangleDrawOptions.filled(Color.BLUE.opacity(0.9)));
            //TODO handle zoom changes
        }
    }

    private BufferedImage createImage() {
        BufferedImage image = new BufferedImage(simulation.size(), simulation.size(), BufferedImage.TYPE_INT_ARGB);//TODO image ops
        var pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        int width = image.getWidth();

        for (int y = 0; y < image.getHeight(); y++) {
            int pixelIndex = y * width;
            for (int x = 0; x < width; x++) {

                int r = (int) (Math.clamp(simulation.density(x, y), 0, 1.0) * 255);
                int g = r;
                int b = 50;
                int a = 128;
                pixels[pixelIndex + x] = (a << 24) | (r << 16) | (g << 8) | b;
            }
        }
        //                ImageOperations.blurImage(image, 3);
        return image;
    }
}
