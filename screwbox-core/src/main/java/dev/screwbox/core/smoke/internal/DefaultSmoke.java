package dev.screwbox.core.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.internal.ImageOperations;
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
    private int cellSize = 12;
    private int screenBorder = 128;
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
        var lastAnchor = worldAnchor;
        var boundsArea = calculateBestBounds();

        // 1. Snapping wie gehabt, um Sub-Pixel-Zittern zu vermeiden
        double snappedX = Math.round(boundsArea.origin().x() / cellSize) * cellSize;
        double snappedY = Math.round(boundsArea.origin().y() / cellSize) * cellSize;
        worldAnchor = Vector.of(snappedX, snappedY);

        var oldSimulation = simulation;
        // Hier erlauben wir die dynamische Größenänderung explizit!
        int newCells = (int) Math.round(boundsArea.width() / cellSize);
        simulation = new FluidSimulation(newCells);

        if (lastAnchor != null) {
            // 2. MATHEMATISCH KORREKTES DELTA BEI GRÖSSENÄNDERUNG:
            // Wir berechnen, wie viele Zellen die NEUE linke obere Ecke von der ALTEN linken oberen Ecke entfernt ist.
            // Das gleicht eine Expansion/Kontraktion des Gitters perfekt aus.
            int deltaX = (int) Math.round((worldAnchor.x() - lastAnchor.x()) / cellSize);
            int deltaY = (int) Math.round((worldAnchor.y() - lastAnchor.y()) / cellSize);

            // Wir übergeben die reinen Deltas direkt an die neue loadFrom-Methode
            simulation.loadFrom(oldSimulation, deltaX, deltaY);
        }
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
    public Smoke emit(Vector position, double amount, Color color) {
        var cell = toCell(position);
        simulation.addDensity(cell.x(), cell.y(), amount, color);
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

    static Time lastUpdate = Time.now();
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
            if (updateTask != null) {
                try {
                    updateTask.get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
            updateTask = (FutureTask<?>) executor.submit(() -> {
                simulation.step(0.002, 0.0004, 0.0003, 6);
                var delta =  (double)Duration.since(lastUpdate).nanos()/ (double)Time.Unit.SECONDS.nanos();
                simulation.fade(delta/10.0);
                lastUpdate = Time.now();
            });
            double scale = cellSize * viewportManager.defaultViewport().camera().zoom()/upscale;
            Offset origin = viewportManager.defaultViewport().toCanvas(worldAnchor);
            viewportManager.defaultViewport().canvas().drawSprite(image, origin, SpriteDrawOptions
                .scaled(scale)
                    .opacity(0.5)//TODO config
                .drawOrder(Order.PRESENTATION_WORLD.drawOrder() + drawOrder));//TODO size
            //TODO handle zoom changes
        }

    }

    private static int upscale = 4;
    private static int blur = 4;

    private static Sprite createImage(DensityInfo densityInfo) {
        BufferedImage image = new BufferedImage(densityInfo.cells()*upscale, densityInfo.cells()*upscale, BufferedImage.TYPE_INT_ARGB);//TODO image ops
        var pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        int width = image.getWidth();

        for (int y = 0; y < image.getHeight(); y++) {
            int pixelIndex = y * width;
            for (int x = 0; x < width; x++) {

                int r = (int) (Math.clamp(densityInfo.dessityRAt(x/upscale, y/upscale), 0, 1.0) * 255);
                int g =(int) (Math.clamp(densityInfo.dessityGAt(x/upscale, y/upscale), 0, 1.0) * 255);
                int b = (int) (Math.clamp(densityInfo.dessityBAt(x/upscale, y/upscale), 0, 1.0) * 255);
                int a = Math.min(255, (r + g + b));
                pixels[pixelIndex + x] = (a << 24) | (r << 16) | (g << 8) | b;
            }
        }
        if(blur > 0) {
            ImageOperations.blurImage(image, blur);
        }
        return Sprite.fromImage(image);
    }
}
