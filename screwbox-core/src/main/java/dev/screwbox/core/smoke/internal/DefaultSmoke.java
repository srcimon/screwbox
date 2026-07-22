package dev.screwbox.core.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.internal.ViewportManager;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.smoke.Smoke;
import dev.screwbox.core.loop.internal.Updatable;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class DefaultSmoke implements Smoke, Updatable {

//TODO support split screen
    private final ViewportManager viewportManager;
    private int cellSize = 8;
    private int screenBorder=32;

    private Vector lastFocus;
    private FluidSimulation simulation;

    public DefaultSmoke(final ViewportManager viewportManager) {
        this.viewportManager = viewportManager;
    }

    @Override
    public Smoke enable() {
        Bounds visibleArea = viewportManager.defaultViewport().visibleArea();
        var size = (int)Math.max(
            Math.ceil((visibleArea.width() + 2 * screenBorder) / cellSize),
            Math.ceil((visibleArea.height() + 2 * screenBorder) / cellSize));
        simulation = new FluidSimulation(size);
        lastFocus = snapFocus();
        return this;
    }

    private Vector snapFocus() {
        return viewportManager.defaultViewport().camera().focus().snap(cellSize);
    }

    @Override
    public Smoke disable() {
        simulation = null;
        lastFocus = null;
        return this;
    }

    @Override
    public void update() {
        if(simulation != null) {
            //TODO get delta from update()
            simulation.step(0.02 , 0.4,0.3, 4);
            var focus = snapFocus();
            if(focus.distanceTo(lastFocus) > 0) {
                lastFocus = focus;
            }
            var worldFocus = viewportManager.defaultViewport().camera().focus();
            simulation.addDensity(20,20, 100);
            simulation.addVelocity(20,20, 140,0);
            BufferedImage image = createImage();
            viewportManager.defaultViewport().canvas().drawSprite(Sprite.fromImage(image), Offset.at(0,0), SpriteDrawOptions
                .scaled(viewportManager.defaultViewport().camera().zoom())
                .drawOrder(Order.DEBUG_OVERLAY.drawOrder()));//TODO size
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
                int b = r;
                int a = r;
                pixels[pixelIndex + x] = (a << 24) | (r << 16) | (g << 8) | b;
            }
        }
        //                ImageOperations.blurImage(image, 3);
        return image;
    }
}
