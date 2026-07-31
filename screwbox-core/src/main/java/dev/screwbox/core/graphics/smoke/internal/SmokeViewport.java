package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.smoke.SmokeOptions;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.util.Objects.nonNull;

public class SmokeViewport {

    private final Viewport viewport;
    private final GraphicsConfiguration configuration;
    private final ExecutorService executor;
    private final SmokeRenderer smokeRender;

    private Future<?> simulationTask;
    private Vector worldAnchor = Vector.zero();
    private Vector imageWorldAnchor = Vector.zero();
    private FluidSimulation simulation;

    public SmokeViewport(ExecutorService executor, Viewport viewport, GraphicsConfiguration configuration, SmokeRenderer smokeRender) {
        this.viewport = viewport;
        this.configuration = configuration;
        this.executor = executor;
        this.smokeRender = smokeRender;
    }

    void render(SmokeOptions options, double delta, List<Bounds> obstacles, List<DensityChange> densityChanges, List<VelocityChange> velocityChanges) {
        if (simulation == null) {
            reassignGrid();
        }
        if (configuration.isSmokeEnabled()) {
            imageWorldAnchor = worldAnchor;
            awaitEndOfSimulationTask();
            simulation.clearObstacles();

            for (final var obstacle : obstacles) {
                var origin = toCell(obstacle.origin());
                var max = toCell(obstacle.bottomRight());
                for (int x = origin.x(); x < max.x(); x++) {
                    for (int y = origin.y(); y < max.y(); y++) {
                        simulation.setObstacle(x, y);
                    }
                }
            }

            for (final var densityChange : densityChanges) {
                final var cell = toCell(densityChange.position());
                simulation.addDensity(cell, densityChange.amount(), densityChange.color());
            }

            for (final var velocityChange : velocityChanges) {
                var cell = toCell(velocityChange.position());
                simulation.addVelocity(cell, velocityChange.velocity());
            }

            final var state = simulation.state();
            simulationTask = executor.submit(() -> {
                simulation.step(delta, options.viscosity(), options.diffusion(), options.iterations());
                simulation.fade(delta * options.fade().value());
            });


            var actuallyVisibleBounds = calculateActuallyVisibleBounds();
            final var sprite = Asset.asset(() -> smokeRender.createImage(configuration, state, actuallyVisibleBounds));
            executor.submit(sprite::get);
            int cellSize = configuration.smokeCellSize();
            final double scale = cellSize * viewport.camera().zoom() / configuration.smokeScale();
            final Offset origin = viewport.toCanvas(imageWorldAnchor).add(viewport.canvas().offset()).add((int) (actuallyVisibleBounds.x() * cellSize * viewport.camera().zoom()), (int) (actuallyVisibleBounds.y() * cellSize * viewport.camera().zoom()));
            viewport.canvas().drawSprite(sprite, origin, SpriteDrawOptions
                .scaled(scale));
            if (!calculateFluidOnWorld().contains(viewport.visibleArea().expand(cellSize * configuration.smokeCellPadding() * 0.5))) {
                reassignGrid();
            } //TODO do not render empty images
        }
    }

    private Offset toCell(final Vector position) {
        final var cellX = Math.floor((position.x() - worldAnchor.x()) / configuration.smokeCellSize());
        final var cellY = Math.floor((position.y() - worldAnchor.y()) / configuration.smokeCellSize());
        return Offset.at(cellX, cellY);
    }

    private void awaitEndOfSimulationTask() {
        if (nonNull(simulationTask)) {
            try {
                simulationTask.get();
            } catch (final InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("error updating fluid simulation", e);
            }
        }
    }

    private ScreenBounds calculateActuallyVisibleBounds() {
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
        int startCellX = (int) Math.floor(gridMinX / configuration.smokeCellSize());
        int startCellY = (int) Math.floor(gridMinY / configuration.smokeCellSize());
        int endCellX = (int) Math.ceil(gridMaxX / configuration.smokeCellSize());
        int endCellY = (int) Math.ceil(gridMaxY / configuration.smokeCellSize());

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
        return Bounds.atOrigin(worldAnchor, (double) configuration.smokeCellSize() * simulation.resolution(), (double) configuration.smokeCellSize() * simulation.resolution());
    }

    private Bounds calculateBestBounds() {
        final var bestBounds = viewport.visibleArea().expand((double) configuration.smokeCellPadding() * configuration.smokeCellSize()).snapExpand(configuration.smokeCellSize());
        return bestBounds.resize(
            Math.max(bestBounds.width(), bestBounds.height()),
            Math.max(bestBounds.width(), bestBounds.height()));
    }

    private void reassignGrid() {
        awaitEndOfSimulationTask();
        var lastAnchor = worldAnchor;
        var boundsArea = calculateBestBounds();
        int cellSize = configuration.smokeCellSize();
        // 1. Snapping wie gehabt, um Sub-Pixel-Zittern zu vermeiden
        long snappedX = Math.round(boundsArea.origin().x() / cellSize) * cellSize;
        long snappedY = Math.round(boundsArea.origin().y() / cellSize) * cellSize;
        worldAnchor = Vector.of(snappedX, snappedY);

        var oldSimulation = simulation;
        // Hier erlauben wir die dynamische Größenänderung explizit!
        int resolution = (int) Math.round(boundsArea.width() / cellSize);
        simulation = new FluidSimulation(resolution);
        if (nonNull(lastAnchor)) {
            // 2. MATHEMATISCH KORREKTES DELTA BEI GRÖSSENÄNDERUNG:
            // Wir berechnen, wie viele Zellen die NEUE linke obere Ecke von der ALTEN linken oberen Ecke entfernt ist.
            // Das gleicht eine Expansion/Kontraktion des Gitters perfekt aus.
            int deltaX = (int) Math.round((worldAnchor.x() - lastAnchor.x()) / cellSize);
            int deltaY = (int) Math.round((worldAnchor.y() - lastAnchor.y()) / cellSize);

            // Wir übergeben die reinen Deltas direkt an die neue loadFrom-Methode
            if (nonNull(oldSimulation)) {
                simulation.loadFrom(oldSimulation, deltaX, deltaY);//TODO load from densityInfo
            }
        }
    }
}
