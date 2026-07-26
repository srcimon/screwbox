package dev.screwbox.core.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.internal.ViewportManager;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.loop.internal.DefaultLoop;
import dev.screwbox.core.smoke.Smoke;

import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DefaultSmoke implements Smoke {

    //TODO support split screen
    private final ViewportManager viewportManager;
    private final ExecutorService executor;
    private int cellSize = 10;
    private int screenBorderCells = 20;
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
        Bounds visibleArea = viewportManager.defaultViewport().visibleArea().expand(screenBorderCells * cellSize);//TODO remove expand
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
        if (simulationTask != null) {
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
            if (!calculateFluidOnWorld().contains(viewportManager.defaultViewport().visibleArea().expand(cellSize * screenBorderCells * 0.5))) {
                reassignGrid();
            }
        });


        DensityInfo densityInfo = simulation.densityInfo();
        final var sprite = Asset.asset(() -> createImage(densityInfo));
        executor.submit(sprite::get);
        final double scale = cellSize * viewportManager.defaultViewport().camera().zoom() / upscale;
        final Offset origin = viewportManager.defaultViewport().toCanvas(imageWorldAnchor);
        viewportManager.defaultViewport().canvas().drawSprite(sprite, origin, SpriteDrawOptions
            .scaled(scale));

    }

    private Bounds calculateFluidOnWorld() {
        return Bounds.atOrigin(worldAnchor, cellSize * simulation.size(), cellSize * simulation.size());
    }

    private static int upscale = 5;
    private static int blur = 5;

    static Percent maxOpacity = Percent.max();

    //TODO reuse bufferimage
    //TODO only switch grid size when resolution changes
    //TODO only create image from visible cells
    //TODO do not render image when empty
    private static Sprite createImage(DensityInfo densityInfo) {
        int maxOpacityva = maxOpacity.rangeValue(0, 255);
        int cells = densityInfo.cells();
        int targetSize = cells * upscale;
        var image = ImageOperations.createImage(Size.square(targetSize));//TODO reuse
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

        // 1. Look-Up-Tabellen (LUT) für X-Achse vorbereiten (Vermeidet double-Arithmetik in der inneren Schleife)
        int[] x0Arr = new int[targetSize];
        int[] x1Arr = new int[targetSize];
        float[] tXArr = new float[targetSize];

        for (int x = 0; x < targetSize; x++) {
            float srcX = (float) x / upscale;
            int x0 = (int) srcX; // Math.floor nicht nötig bei positiven Zahlen
            x0Arr[x] = x0;
            x1Arr[x] = Math.min(cells - 1, x0 + 1);
            tXArr[x] = srcX - x0;
        }

        // 2. Hauptschleife mit optimierter Interpolation
        for (int y = 0; y < targetSize; y++) {
            int pixelIndex = y * targetSize;

            float srcY = (float) y / upscale;
            int y0 = (int) srcY;
            int y1 = Math.min(cells - 1, y0 + 1);
            float tY = srcY - y0;
            float invTY = 1.0f - tY;

            for (int x = 0; x < targetSize; x++) {
                int x0 = x0Arr[x];
                int x1 = x1Arr[x];
                float tX = tXArr[x];
                float invTX = 1.0f - tX;

                // Gewichtungen vorab berechnen
                float w00 = invTX * invTY;
                float w10 = tX * invTY;
                float w01 = invTX * tY;
                float w11 = tX * tY;

                // Rot-Kanal (Inlined Interpolation)
                float r = (float) (densityInfo.dessityRAt(x0, y0) * w00 +
                                   densityInfo.dessityRAt(x1, y0) * w10 +
                                   densityInfo.dessityRAt(x0, y1) * w01 +
                                   densityInfo.dessityRAt(x1, y1) * w11);

                // Grün-Kanal
                float g = (float) (densityInfo.dessityGAt(x0, y0) * w00 +
                                   densityInfo.dessityGAt(x1, y0) * w10 +
                                   densityInfo.dessityGAt(x0, y1) * w01 +
                                   densityInfo.dessityGAt(x1, y1) * w11);

                // Blau-Kanal
                float b = (float) (densityInfo.dessityBAt(x0, y0) * w00 +
                                   densityInfo.dessityBAt(x1, y0) * w10 +
                                   densityInfo.dessityBAt(x0, y1) * w01 +
                                   densityInfo.dessityBAt(x1, y1) * w11);

                // Skalieren & Clamping (Manuelles Math.min/max ist schneller als Math.clamp)
                int rInt = (int) (r * 255);
                rInt = rInt < 0 ? 0 : (Math.min(rInt, 255));

                int gInt = (int) (g * 255);
                gInt = gInt < 0 ? 0 : (Math.min(gInt, 255));

                int bInt = (int) (b * 255);
                bInt = bInt < 0 ? 0 : (Math.min(bInt, 255));

                // Alpha-Berechnung
                int maxRGB = rInt > gInt ? rInt : gInt;
                if (bInt > maxRGB) maxRGB = bInt;
                int aInt = maxRGB > maxOpacityva ? maxOpacityva : maxRGB;

                pixels[pixelIndex + x] = (aInt << 24) | (rInt << 16) | (gInt << 8) | bInt;
            }
        }

        if (blur > 0) {
            ImageOperations.blurImage(image, blur);
        }

        return Sprite.fromImage(image);
    }

}
