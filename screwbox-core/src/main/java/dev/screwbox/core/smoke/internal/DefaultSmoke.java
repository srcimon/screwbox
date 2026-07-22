package dev.screwbox.core.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.internal.ViewportManager;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.loop.internal.Updatable;
import dev.screwbox.core.smoke.Smoke;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class DefaultSmoke implements Smoke, Updatable {

    //TODO support split screen
    private final ViewportManager viewportManager;
    private int cellSize = 2;
    private int screenBorder = 32;

    private Vector worldAnchor;
    private FluidSimulation simulation;

    public DefaultSmoke(final ViewportManager viewportManager) {
        this.viewportManager = viewportManager;
    }

    @Override
    public Smoke enable() {
        reassignGrid();
        return this;
    }

    private void reassignGrid() {
        var boundsArea = calculateBestBounds();
        worldAnchor = boundsArea.origin();
        simulation = new FluidSimulation((int) (boundsArea.width() / cellSize));
    }

    private Bounds calculateBestBounds() {
        Bounds visibleArea = viewportManager.defaultViewport().visibleArea().expand(screenBorder);//TODO remove expand
        var boundsArea = visibleArea.snapExpand(cellSize);
        return boundsArea.resize(
            Math.max(boundsArea.width(), boundsArea.height()),
            Math.max(boundsArea.width(), boundsArea.height()));
    }

    @Override
    public Smoke disable() {
        simulation = null;
        worldAnchor = null;
        return this;
    }

    @Override
    public Smoke emit(Vector position, double amount) {
        var cell = toCell(position);
        simulation.addDensity(cell.x(), cell.y(), amount);
        return this;
    }

    private Offset toCell(Vector position) {
        var cellX = Math.floor((position.x() - worldAnchor.x()) / cellSize);
        var cellY = Math.floor((position.y() - worldAnchor.y()) / cellSize);
        return Offset.at(cellX, cellY);
    }

    @Override
    public Smoke affect(Vector position, Vector velocity) {
        var cell = toCell(position);
        simulation.addVelocity(cell.x(), cell.y(), velocity.x(), velocity.y());//TODO apply x and y fixes
        return this;
    }

    @Override
    public void update() {
        if (simulation != null) {
            if (calculateBestBounds().origin().distanceTo(worldAnchor) > screenBorder / 2.0) {//TODO > border
                reassignGrid();
            }
            //TODO get delta from update()
            simulation.step(0.002, 0.0004, 0.0003, 4);
            simulation.fade(0.0005);
//            simulation.addDensity(30,30, 2);
//            simulation.addVelocity(30,30, 8,8);

            BufferedImage image = createImage();

            double scale = cellSize * viewportManager.defaultViewport().camera().zoom();
            Offset origin = viewportManager.defaultViewport().toCanvas(worldAnchor);
            viewportManager.defaultViewport().canvas().drawSprite(Sprite.fromImage(image), origin, SpriteDrawOptions
                .scaled(scale)
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
//                        ImageOperations.blurImage(image, 3);
        return image;
    }
}
