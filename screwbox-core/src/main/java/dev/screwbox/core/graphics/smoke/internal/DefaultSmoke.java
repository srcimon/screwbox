package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.internal.ViewportManager;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.smoke.Smoke;
import dev.screwbox.core.loop.internal.DefaultLoop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DefaultSmoke implements Smoke {
    private static int upscale = 6;
    private static int blur = 4;

    static Percent maxOpacity = Percent.of(1);

    //TODO support split screen
    private final ViewportManager viewportManager;
    private final ExecutorService executor;
    private final SmokeRenderer smokeRender;
    private int cellSize = 8;
    private int screenBorderCells = 32;

    private Vector worldAnchor;
    private Vector imageWorldAnchor = Vector.zero();
    private FluidSimulation simulation;

    public DefaultSmoke(final ViewportManager viewportManager, ExecutorService executor) {
        this.viewportManager = viewportManager;
        this.executor = executor;
        this.smokeRender = new SmokeRenderer();
    }

    @Override
    public Smoke enable() {
        reassignGrid();
        return this;
    }

    private void reassignGrid() {
        awaitEndOfSimulationTask();
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
        var cell = toCell(position);//TODO no emission on obstacles
        if (cell.x() > 2 && cell.y() > 2 && cell.x() < simulation.size() - 2 && cell.y() < simulation.size() - 2) {
            tasks.add(() -> simulation.addDensity(cell.x(), cell.y(), amount, color));
        }

        return this;
    }

    @Override
    public Smoke push(Vector position, Vector velocity) {
        var cell = toCell(position);//TODO no emission on obstacles
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
        simulation.clearObstacles();
        for (var task : tasks) {
            task.run();
        }
        tasks.clear();
        awaitEndOfSimulationTask();

        simulationTask = executor.submit(() -> {
            simulation.step(de, 0.0000000004, 0.000001, 2);
            simulation.fade(de * 0.04);
        });


        DensityInfo densityInfo = simulation.densityInfo();
        var actuallyVisibleBounds = calculateActuallyVisibleBounds();
        final var sprite = Asset.asset(() -> smokeRender.createImage(blur, upscale, maxOpacity, densityInfo, actuallyVisibleBounds));
        executor.submit(sprite::get);
        final double scale = cellSize * viewportManager.defaultViewport().camera().zoom() / upscale;
        final Offset origin = viewportManager.defaultViewport().toCanvas(imageWorldAnchor).add((int) (actuallyVisibleBounds.x() * cellSize * viewportManager.defaultViewport().camera().zoom()), (int) (actuallyVisibleBounds.y() * cellSize * viewportManager.defaultViewport().camera().zoom()));
        viewportManager.defaultViewport().canvas().drawSprite(sprite, origin, SpriteDrawOptions
            .scaled(scale));
        if (!calculateFluidOnWorld().contains(viewportManager.defaultViewport().visibleArea().expand(cellSize * screenBorderCells * 0.5))) {
            reassignGrid();
        }
    }

    @Override
    public Smoke addObstacle(Bounds bounds) {
        tasks.add(() -> {
            var origin = toCell(bounds.origin());
            var max = toCell(bounds.bottomRight());
            for (int x = origin.x(); x < max.x(); x++) {
                for (int y = origin.y(); y < max.y(); y++) {
                    simulation.setObstacle(x, y, true);
                }
            }
        });
        return this;
    }

    private void awaitEndOfSimulationTask() {
        if (simulationTask != null) {
            try {
                simulationTask.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ScreenBounds calculateActuallyVisibleBounds() {
        final var viewport = viewportManager.defaultViewport();

        // 1. Berechne die sichtbare Welt-Fläche (unabhängig vom Zoom/Pixeln)
        final var visibleArea = viewport.visibleArea();
        final double viewMinX = visibleArea.minX();
        final double viewMinY = visibleArea.minY();
        final double viewMaxX = viewMinX + visibleArea.width();
        final double viewMaxY = viewMinY + visibleArea.height();

        // 2. Ermittle die Welt-Koordinaten relativ zum Ursprung des Gitters
        final double gridMinX = viewMinX - imageWorldAnchor.x();
        final double gridMinY = viewMinY - imageWorldAnchor.y();
        final double gridMaxX = viewMaxX - imageWorldAnchor.x();
        final double gridMaxY = viewMaxY - imageWorldAnchor.y();

        // 3. Bestimme die exakten Start- und End-Zellen (Abrunden/Aufrunden via Double)
        int startCellX = (int) Math.floor(gridMinX / cellSize);
        int startCellY = (int) Math.floor(gridMinY / cellSize);
        int endCellX = (int) Math.ceil(gridMaxX / cellSize);
        int endCellY = (int) Math.ceil(gridMaxY / cellSize);

        // 4. Füge den gewünschten Sicherheitsabstand (1 Zelle Puffer rundherum) hinzu
        startCellX = startCellX - 1;
        startCellY = startCellY - 1;
        endCellX = endCellX + 1;
        endCellY = endCellY + 1;

        // 5. Striktes Clamping an die physikalischen Simulationsgrenzen
        final int maxCells = simulation.size();
        startCellX = Math.clamp(startCellX, 0, maxCells - 1);
        startCellY = Math.clamp(startCellY, 0, maxCells - 1);
        endCellX = Math.clamp(endCellX, startCellX + 1, maxCells);
        endCellY = Math.clamp(endCellY, startCellY + 1, maxCells);

        int width = endCellX - startCellX;
        int height = endCellY - startCellY;

        return new ScreenBounds(Offset.origin().add(startCellX, startCellY), Size.of(width, height));
    }

    private Bounds calculateFluidOnWorld() {
        return Bounds.atOrigin(worldAnchor, cellSize * simulation.size(), cellSize * simulation.size());
    }


}
