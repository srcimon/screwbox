package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.internal.ViewportManager;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.smoke.Smoke;
import dev.screwbox.core.utils.Validate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

//TODO add feature buble to webpage

//TODO blog on smoke
public class DefaultSmoke implements Smoke {

    private record DensityChange(Offset cell, double amount, Color color) {

    }

    private record VelocityChange(Offset cell, Vector velocity) {
    }

    private static int upscale = 6;
    private static int blur = 4;
    private static Percent maxOpacity = Percent.of(1);
    private static int cellSize = 8;
    private static int screenBorderCells = 32;

    private static double maxDensity = 4;
    private static double maxVelocity = 20;

    //TODO support split screen!!!!!!!!!!!!!!!!!
    private final ViewportManager viewportManager;
    private final ExecutorService executor;
    private final SmokeRenderer smokeRender;
    private final GraphicsConfiguration configuration;

    private final List<Bounds> obstacles = new ArrayList<>();
    private final List<DensityChange> densityChanges = new ArrayList<>();
    private final List<VelocityChange> velocityChanges = new ArrayList<>();


    Future<?> simulationTask;
    private Vector worldAnchor = Vector.zero();
    private Vector imageWorldAnchor = Vector.zero();
    private FluidSimulation simulation;

    public DefaultSmoke(final ViewportManager viewportManager, final GraphicsConfiguration configuration, final ExecutorService executor, SmokeRenderer smokeRender) {
        this.viewportManager = viewportManager;
        this.executor = executor;
        this.smokeRender = smokeRender;
        this.configuration = configuration;
    }


    @Override
    public Smoke emit(final Vector position, final Vector velocity, final double amount, final Color color) {
        emit(position, velocity, amount, color);
        push(position, velocity);
        return this;
    }

    @Override
    public Smoke emit(final Vector position, final double amount, final Color color) {
        Validate.zeroOrPositive(amount, "amount must be positive");
        var cell = toCell(position);
        densityChanges.add(new DensityChange(cell, amount, color));
        return this;
    }

    @Override
    public Smoke push(final Vector position, final Vector velocity) {
        var cell = toCell(position);
        velocityChanges.add(new VelocityChange(cell, velocity));
        return this;
    }

    private Offset toCell(final Vector position) {
        var cellX = Math.floor((position.x() - worldAnchor.x()) / cellSize);
        var cellY = Math.floor((position.y() - worldAnchor.y()) / cellSize);
        return Offset.at(cellX, cellY);
    }

    @Override
    public Smoke render(final double delta) {
        if (simulation == null) {
            reassignGrid();
        }
        if (configuration.isSmokeEnabled()) {


            //TODO get delta from update()
            imageWorldAnchor = worldAnchor;
            awaitEndOfSimulationTask();
            simulation.clearObstacles();

            for (var obstacle : obstacles) {
                var origin = toCell(obstacle.origin());
                var max = toCell(obstacle.bottomRight());
                for (int x = origin.x(); x < max.x(); x++) {
                    for (int y = origin.y(); y < max.y(); y++) {
                        simulation.setObstacle(x, y);
                    }
                }
            }
            obstacles.clear();

            for (final var densityChange : densityChanges) {
                simulation.addDensity(densityChange.cell, densityChange.amount, maxDensity, densityChange.color);
            }
            densityChanges.clear();
            for (final var velocityChange : velocityChanges) {
                simulation.addVelocity(velocityChange.cell(), velocityChange.velocity(), maxVelocity);
            }
            velocityChanges.clear();

            simulationTask = executor.submit(() -> {
                simulation.step(delta, 0.0000000004, 0.000001, 2);
                simulation.fade(delta * 0.04);
            });


            FluidSimulationState fluidSimulationState = simulation.state();
            var actuallyVisibleBounds = calculateActuallyVisibleBounds();
            final var sprite = Asset.asset(() -> smokeRender.createImage(blur, upscale, maxOpacity, fluidSimulationState, actuallyVisibleBounds));
            executor.submit(sprite::get);
            final double scale = cellSize * viewportManager.defaultViewport().camera().zoom() / upscale;
            final Offset origin = viewportManager.defaultViewport().toCanvas(imageWorldAnchor).add((int) (actuallyVisibleBounds.x() * cellSize * viewportManager.defaultViewport().camera().zoom()), (int) (actuallyVisibleBounds.y() * cellSize * viewportManager.defaultViewport().camera().zoom()));
            viewportManager.defaultViewport().canvas().drawSprite(sprite, origin, SpriteDrawOptions
                .scaled(scale));
            if (!calculateFluidOnWorld().contains(viewportManager.defaultViewport().visibleArea().expand(cellSize * screenBorderCells * 0.5))) {
                reassignGrid();
            }
        }
        return this;
    }

    @Override
    public Smoke addObstacle(final Bounds bounds) {
        //TODO check if within grid?
        obstacles.add(bounds);
        return this;
    }

    private void awaitEndOfSimulationTask() {
        if (simulationTask != null) {
            try {
                simulationTask.get();
            } catch (final InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("error updating fluid simulation", e);
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
        final int maxCells = simulation.resolution();
        startCellX = Math.clamp(startCellX, 0, maxCells - 1);
        startCellY = Math.clamp(startCellY, 0, maxCells - 1);
        endCellX = Math.clamp(endCellX, startCellX + 1, maxCells);
        endCellY = Math.clamp(endCellY, startCellY + 1, maxCells);

        int width = endCellX - startCellX;
        int height = endCellY - startCellY;

        return new ScreenBounds(Offset.origin().add(startCellX, startCellY), Size.of(width, height));
    }

    private Bounds calculateFluidOnWorld() {
        return Bounds.atOrigin(worldAnchor, (double) cellSize * simulation.resolution(), (double) cellSize * simulation.resolution());
    }

    private Bounds calculateBestBounds() {
        final var bestBounds = viewportManager.defaultViewport().visibleArea().expand((double) screenBorderCells * cellSize).snapExpand(cellSize);
        return bestBounds.resize(
            Math.max(bestBounds.width(), bestBounds.height()),
            Math.max(bestBounds.width(), bestBounds.height()));
    }

    private void reassignGrid() {
        awaitEndOfSimulationTask();
        var lastAnchor = worldAnchor;
        var boundsArea = calculateBestBounds();

        // 1. Snapping wie gehabt, um Sub-Pixel-Zittern zu vermeiden
        long snappedX = Math.round(boundsArea.origin().x() / cellSize) * cellSize;
        long snappedY = Math.round(boundsArea.origin().y() / cellSize) * cellSize;
        worldAnchor = Vector.of(snappedX, snappedY);

        var oldSimulation = simulation;
        // Hier erlauben wir die dynamische Größenänderung explizit!
        int resolution = (int) Math.round(boundsArea.width() / cellSize);
        simulation = new FluidSimulation(resolution);
        if (lastAnchor != null) {
            // 2. MATHEMATISCH KORREKTES DELTA BEI GRÖSSENÄNDERUNG:
            // Wir berechnen, wie viele Zellen die NEUE linke obere Ecke von der ALTEN linken oberen Ecke entfernt ist.
            // Das gleicht eine Expansion/Kontraktion des Gitters perfekt aus.
            int deltaX = (int) Math.round((worldAnchor.x() - lastAnchor.x()) / cellSize);
            int deltaY = (int) Math.round((worldAnchor.y() - lastAnchor.y()) / cellSize);

            // Wir übergeben die reinen Deltas direkt an die neue loadFrom-Methode
            if (oldSimulation != null) {
                simulation.loadFrom(oldSimulation, deltaX, deltaY);//TODO load from densityInfo
            }
        }
    }

}
