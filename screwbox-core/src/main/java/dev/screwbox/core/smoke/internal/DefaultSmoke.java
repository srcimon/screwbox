package dev.screwbox.core.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.internal.ViewportManager;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.loop.internal.DefaultLoop;
import dev.screwbox.core.loop.internal.Updatable;
import dev.screwbox.core.smoke.Smoke;
import dev.screwbox.core.utils.PerlinNoise;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class DefaultSmoke implements Smoke, Updatable {

    //TODO support split screen
    private final ViewportManager viewportManager;
    private final ExecutorService executor;
    private int cellSize = 10;
    private int screenBorder = 128;
    private Vector worldAnchor;
    private Vector imageWorldAnchor = Vector.zero();
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


    List<Runnable> tasks = new ArrayList<>();
    @Override
    public Smoke emit(Vector position, double amount, Color color) {
        var cell = toCell(position);
        if (cell.x() > 2 && cell.y() > 2 && cell.x() < simulation.size() - 2 && cell.y() < simulation.size() - 2) {
            tasks.add(() -> simulation.addDensity(cell.x(), cell.y(), amount, color));
        }

        return this;
    }

    @Override
    public Smoke affect(Vector position, Vector velocity) {
        var cell = toCell(position);
        if (cell.x() > 2 && cell.y() > 2 && cell.x() < simulation.size() - 2 && cell.y() < simulation.size() - 2) {
            tasks.add(() -> simulation.addVelocity(cell.x(), cell.y(), velocity.x(), velocity.y()));
        }
        return this;
    }

    private Offset toCell(Vector position) {
        var cellX = Math.floor((position.x() - worldAnchor.x()) / cellSize);
        var cellY = Math.floor((position.y() - worldAnchor.y()) / cellSize);
        return Offset.at(cellX, cellY);
    }

    Future<?> simulationTask;
    @Override
    public void render() {
        if (simulation == null) {
            return;
        }

        //TODO get delta from update()
        imageWorldAnchor = worldAnchor;
        double de = DefaultLoop.DE;
        for (var task : tasks) {
            task.run();
        }
        tasks.clear();
        if(simulationTask != null) {
            try {
                simulationTask.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        simulationTask = executor.submit(() -> {
            simulation.step(de, 0.000004, 0.000000000001, 2);
            simulation.fade(de * 0.04);
        });


        DensityInfo densityInfo = simulation.densityInfo();
        if (calculateBestBounds().origin().distanceTo(worldAnchor) > screenBorder * 0.95) {//TODO > border
            reassignGrid();
        }
        var sprite = Asset.asset(() -> createImage(densityInfo));
        executor.submit(sprite::get);
        double scale = cellSize * viewportManager.defaultViewport().camera().zoom() / upscale;
        Offset origin = viewportManager.defaultViewport().toCanvas(imageWorldAnchor);
        viewportManager.defaultViewport().canvas().drawSprite(sprite, origin, SpriteDrawOptions
            .scaled(scale)
            .opacity(1)); //TODO config

    }

    @Override
    public void update() {
    }

    private static int upscale = 5;
    private static int blur = 5;

    static Percent maxOpacity = Percent.max();
    //TODO reuse bufferimage
    //TODO only switch grid size when resolution changes
    //TODO only create image from visible cells
    private static Sprite createImage(DensityInfo densityInfo) {
        int maxOpacityva = maxOpacity.rangeValue(0,255);
        int cells = densityInfo.cells();
        int targetSize = cells * upscale;

        // 1. Schritt: Das Bild in der Zielgröße erstellen
           var  image = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
        var pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        // 2. Schritt: Generierung mit bilinearer Interpolation der Zelldaten
        for (int y = 0; y < targetSize; y++) {
            int pixelIndex = y * targetSize;

            // Berechne die genaue Fließkomma-Position im Quellgitter
            double srcY = (double) y / upscale;
            int y0 = (int) Math.floor(srcY);
            int y1 = Math.min(cells - 1, y0 + 1);
            double tY = srcY - y0; // Gewichtungfaktor für Y

            for (int x = 0; x < targetSize; x++) {
                double srcX = (double) x / upscale;
                int x0 = (int) Math.floor(srcX);
                int x1 = Math.min(cells - 1, x0 + 1);
                double tX = srcX - x0; // Gewichtungfaktor für X

                // Bilineare Interpolation für jeden Farbkanal einzeln
                double r = interpolate(densityInfo.dessityRAt(x0, y0), densityInfo.dessityRAt(x1, y0),
                    densityInfo.dessityRAt(x0, y1), densityInfo.dessityRAt(x1, y1), tX, tY);

                double g = interpolate(densityInfo.dessityGAt(x0, y0), densityInfo.dessityGAt(x1, y0),
                    densityInfo.dessityGAt(x0, y1), densityInfo.dessityGAt(x1, y1), tX, tY);

                double b = interpolate(densityInfo.dessityBAt(x0, y0), densityInfo.dessityBAt(x1, y0),
                    densityInfo.dessityBAt(x0, y1), densityInfo.dessityBAt(x1, y1), tX, tY);

                // Skalieren auf 0-255
                int rInt = (int) (Math.clamp(r, 0.0, 1.0) * 255);
                int gInt = (int) (Math.clamp(g, 0.0, 1.0) * 255);
                int bInt = (int) (Math.clamp(b, 0.0, 1.0) * 255);


// NEU: Alpha basiert auf der reinen Präsenz von Farbe, nicht auf deren Additivität
                int aInt = Math.min(maxOpacityva, Math.max(rInt, Math.max(gInt, bInt)));

                pixels[pixelIndex + x] = (aInt << 24) | (rInt << 16) | (gInt << 8) | bInt;
            }
        }

        // Optionaler Blur (falls konfiguriert)
        if (blur > 0) {
            ImageOperations.blurImage(image, blur);
        }


        return Sprite.fromImage(image);
    }

    // Hilfsmethode für die bilineare Interpolation
    private static double interpolate(double v00, double v10, double v01, double v11, double tX, double tY) {
        double top = v00 + tX * (v10 - v00);
        double bottom = v01 + tX * (v11 - v01);
        return top + tY * (bottom - top);
    }

}
