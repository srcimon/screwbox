package dev.screwbox.core.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.internal.ViewportManager;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.loop.internal.Updatable;
import dev.screwbox.core.smoke.Smoke;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

public class DefaultSmoke implements Smoke, Updatable {

    //TODO support split screen
    private final ViewportManager viewportManager;
    private final ExecutorService executor;
    private int cellSize = 2;
    private int screenBorder = 32;
    private int drawOrder = 4;//TODO configure
    private DensityInfo densityInfo;
    private FutureTask<?> updateTask;
    private Vector worldAnchor;
    private FluidSimulation simulation;

    public DefaultSmoke(final ViewportManager viewportManager, ExecutorService executor) {
        this.viewportManager = viewportManager;
        this.executor = executor;
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

            densityInfo = simulation.densityInfo();
            Asset<Sprite> image = Asset.asset(() -> createImage(densityInfo));
            executor.submit(image::get);
            if(updateTask!= null) {
                try {
                    updateTask.get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
            updateTask = (FutureTask<?>) executor.submit(() -> {
                simulation.step(0.002, 0.0004, 0.0003, 8);
                simulation.fade(0.0005);
            });
            double scale = cellSize * viewportManager.defaultViewport().camera().zoom();
            Offset origin = viewportManager.defaultViewport().toCanvas(worldAnchor);
            viewportManager.defaultViewport().canvas().drawSprite(image, origin, SpriteDrawOptions
                .scaled(scale)
                .drawOrder(Order.PRESENTATION_WORLD.drawOrder() +drawOrder));//TODO size
            //TODO handle zoom changes
        }

    }

    private static Sprite createImage(DensityInfo densityInfo) {
        BufferedImage image = new BufferedImage(densityInfo.cells(), densityInfo.cells(), BufferedImage.TYPE_INT_ARGB);//TODO image ops
        var pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        int width = image.getWidth();

        for (int y = 0; y < image.getHeight(); y++) {
            int pixelIndex = y * width;
            for (int x = 0; x < width; x++) {

                int r = (int) (Math.clamp(densityInfo.dessityAt(x, y), 0, 1.0) * 255);
                int g = r;
                int b = r;
                int a = r;
                pixels[pixelIndex + x] = (a << 24) | (r << 16) | (g << 8) | b;
            }
        }
//                        ImageOperations.blurImage(image, 3);
        return Sprite.fromImage(image);
    }
}
