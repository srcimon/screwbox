package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.smoke.SmokeOptions;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.util.Objects.nonNull;

public class SmokeProjector {

    private final GraphicsConfiguration configuration;
    private final ExecutorService executor;
    private final SmokeRenderer renderer;

    private Future<?> simulationTask;
    private Vector worldAnchor;
    private FluidSimulation simulation;

    public SmokeProjector(final ExecutorService executor, final GraphicsConfiguration configuration, final SmokeRenderer renderer) {
        this.configuration = configuration;
        this.executor = executor;
        this.renderer = renderer;
        this.worldAnchor = Vector.zero();
    }

    public void render(final Viewport viewport, final SmokeOptions options, final double delta) {
        final var state = simulation.densityData();
        simulationTask = executor.submit(() -> {
            simulation.step(delta, options.viscosity().value(), options.diffusion().value(), options.iterations());
            simulation.fade(delta * options.fade());
            if (nonNull(options.velocity())) {
                final Vector targetVelocity = options.velocity().divide(configuration.smokeCellSize());
                simulation.fadeVelocity(targetVelocity, delta * options.velocityAdaption().value());
            }
        });

        final var visibleBounds = calculateActuallyVisibleBounds(viewport.visibleArea());
        final var sprite = Asset.asset(() -> {
            final var image = renderer.createImage(state, visibleBounds, configuration.smokeScale(), options.style());
            if (configuration.smokeBlur() > 0) {
                ImageOperations.blurImage(image, configuration.smokeBlur());
            }
            return Sprite.fromImage(image);
        });
        executor.submit(sprite::get);
        int cellSize = configuration.smokeCellSize();
        final double scale = cellSize * viewport.camera().zoom() / configuration.smokeScale();
        final Offset origin = viewport.toCanvas(worldAnchor).add((int) (visibleBounds.x() * cellSize * viewport.camera().zoom()), (int) (visibleBounds.y() * cellSize * viewport.camera().zoom()));
        viewport.canvas().drawSprite(sprite, origin, SpriteDrawOptions
            .scaled(scale)
            .opacity(options.opacity()));

        if (!calculateFluidOnWorld().contains(viewport.visibleArea().expand(cellSize * configuration.smokeCellPadding() * 0.5))) {
            reassignGrid(viewport, options.velocity());
        }
    }

    public void applyVelocityZones(final List<VelocityZone> velocityZones) {
        for (final var velocityZone : velocityZones) {
            var origin = toCell(velocityZone.area().origin());
            var max = toCell(velocityZone.area().bottomRight());
            for (int x = origin.x(); x < max.x(); x++) {
                for (int y = origin.y(); y < max.y(); y++) {
                    simulation.setVelocity(x, y, velocityZone.velocity().divide(configuration.smokeCellSize()));
                }
            }
        }
    }

    public void applyVelocityChanges(final List<VelocityChange> velocityChanges) {
        for (final var velocityChange : velocityChanges) {
            var cell = toCell(velocityChange.position());
            final Vector velocity = velocityChange.velocity().divide(configuration.smokeCellSize());
            simulation.addVelocity(cell.x(), cell.y(), velocity);
        }
    }

    public void applyDensityChanges(final List<DensityChange> densityChanges) {
        for (final var densityChange : densityChanges) {
            final var cell = toCell(densityChange.position());
            simulation.addDensity(cell, densityChange.amount(), densityChange.color());
        }
    }

    public void applyAreaVelocityChanges(final List<AreaVelocityChange> areaVelocityChanges) {
        for (final var areaVelocityChange : areaVelocityChanges) {
            var origin = toCell(areaVelocityChange.area().origin());
            var max = toCell(areaVelocityChange.area().bottomRight());
            for (int x = origin.x(); x < max.x(); x++) {
                for (int y = origin.y(); y < max.y(); y++) {
                    simulation.addVelocity(x, y, areaVelocityChange.velocity());
                }
            }
        }
    }

    public void applyObstacles(List<Bounds> obstacles) {
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
    }

    public void adaptToViewport(Viewport viewport, Vector baseVelocity) {
        if (simulation == null) {
            reassignGrid(viewport, baseVelocity);
        }
        awaitSimulationStep();
    }

    private Offset toCell(final Vector position) {
        final var cellX = Math.floor((position.x() - worldAnchor.x()) / configuration.smokeCellSize());
        final var cellY = Math.floor((position.y() - worldAnchor.y()) / configuration.smokeCellSize());
        return Offset.at(cellX, cellY);
    }

    private void awaitSimulationStep() {
        if (nonNull(simulationTask)) {
            try {
                simulationTask.get();
            } catch (final InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("error updating fluid simulation", e);
            }
        }
    }

    private ScreenBounds calculateActuallyVisibleBounds(final Bounds visibleArea) {
        final double viewMinX = visibleArea.minX();
        final double viewMinY = visibleArea.minY();
        final double viewMaxX = viewMinX + visibleArea.width();
        final double viewMaxY = viewMinY + visibleArea.height();

        final double gridMinX = viewMinX - worldAnchor.x();
        final double gridMinY = viewMinY - worldAnchor.y();
        final double gridMaxX = viewMaxX - worldAnchor.x();
        final double gridMaxY = viewMaxY - worldAnchor.y();

        final int maxCells = simulation.resolution();
        final int startCellX = Math.clamp((int) Math.floor(gridMinX / configuration.smokeCellSize()) - 1, 0, maxCells - 1);
        final int startCellY = Math.clamp((int) Math.floor(gridMinY / configuration.smokeCellSize()) - 1, 0, maxCells - 1);
        final int endCellX = Math.clamp((int) Math.ceil(gridMaxX / configuration.smokeCellSize()) + 1, startCellX + 1, maxCells);
        final int endCellY = Math.clamp((int) Math.ceil(gridMaxY / configuration.smokeCellSize()) + 1, startCellY + 1, maxCells);

        final Size size = Size.of(endCellX - startCellX, endCellY - startCellY);
        return new ScreenBounds(Offset.origin().add(startCellX, startCellY), size);
    }

    private Bounds calculateFluidOnWorld() {
        return Bounds.atOrigin(worldAnchor, (double) configuration.smokeCellSize() * simulation.resolution(), (double) configuration.smokeCellSize() * simulation.resolution());
    }

    private Bounds calculateBestBounds(Viewport viewport) {
        final var bestBounds = viewport.visibleArea().expand((double) configuration.smokeCellPadding() * configuration.smokeCellSize()).snapExpand(configuration.smokeCellSize());
        return bestBounds.resize(
            Math.max(bestBounds.width(), bestBounds.height()),
            Math.max(bestBounds.width(), bestBounds.height()));
    }

    private void reassignGrid(Viewport viewport, Vector baseVelocity) {
        awaitSimulationStep();
        final var lastAnchor = worldAnchor;
        final var boundsArea = calculateBestBounds(viewport);
        final long snappedX = Math.round(boundsArea.origin().x() / configuration.smokeCellSize()) * configuration.smokeCellSize();
        final long snappedY = Math.round(boundsArea.origin().y() / configuration.smokeCellSize()) * configuration.smokeCellSize();
        worldAnchor = Vector.of(snappedX, snappedY);

        var oldSimulation = simulation;
        final int resolution = (int) Math.round(boundsArea.width() / configuration.smokeCellSize());
        simulation = new FluidSimulation(resolution);
        simulation.fillVelocity(baseVelocity);
        if (nonNull(lastAnchor)) {
            final int deltaX = (int) Math.round((worldAnchor.x() - lastAnchor.x()) / configuration.smokeCellSize());
            final int deltaY = (int) Math.round((worldAnchor.y() - lastAnchor.y()) / configuration.smokeCellSize());

            if (nonNull(oldSimulation)) {
                simulation.loadFrom(oldSimulation, deltaX, deltaY);
            }
        }
    }
}
