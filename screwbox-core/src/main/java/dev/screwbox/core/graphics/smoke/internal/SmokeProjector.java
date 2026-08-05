package dev.screwbox.core.graphics.smoke.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.GraphicsConfiguration;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.ScreenBounds;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.internal.ImageOperations;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.smoke.SmokeOptions;
import dev.screwbox.core.utils.Latch;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class SmokeProjector {

    private final GraphicsConfiguration configuration;
    private final ExecutorService executor;
    private final Latch<SmokeRenderer> renderer;

    private Future<?> simulationTask;
    private Vector worldAnchor;
    private FluidSimulation simulation;

    public record VelocityZone(Bounds area, Vector velocity) {
    }

    public record VelocityChange(Vector position, Vector velocity) {
    }

    public record DensityChange(Vector position, double amount, Color color) {
    }

    public record AreaVelocityChange(Bounds area, Vector velocity) {
    }

    public SmokeProjector(final ExecutorService executor, final GraphicsConfiguration configuration) {
        this.configuration = configuration;
        this.executor = executor;
        this.renderer = Latch.of(new SmokeRenderer(), new SmokeRenderer());
        this.worldAnchor = Vector.zero();
    }

    public void render(final Viewport viewport, final SmokeOptions options, final double delta) {
        final var densityData = simulation.densityData();
        if (simulation.hasDensity()) {
            simulationTask = executor.submit(() -> {
                simulation.step(delta, options.viscosity().value(), options.diffusion().value(), options.iterations());
                simulation.fade(delta * options.fade());
            });

            final var visibleBounds = calculateActuallyVisibleBounds(viewport.visibleArea());
            final SmokeRenderer activeRenderer = renderer.active();
            final var sprite = Asset.asset(() -> {
                final var image = activeRenderer.renderSmoke(densityData, visibleBounds, configuration.smokeScale(), options.style());
                if (configuration.smokeBlur() > 0) {
                    ImageOperations.blurImage(image, configuration.smokeBlur());
                }
                return Sprite.fromImage(image);
            });
            renderer.toggle();
            executor.submit(sprite::get);
            final double scale = configuration.smokeCellSize() * viewport.camera().zoom() / configuration.smokeScale();
            final Offset origin = viewport.toCanvas(worldAnchor).add((int) (visibleBounds.x() * configuration.smokeCellSize() * viewport.camera().zoom()), (int) (visibleBounds.y() * configuration.smokeCellSize() * viewport.camera().zoom()));
            viewport.canvas().drawSprite(sprite, origin, SpriteDrawOptions
                .scaled(scale)
                .opacity(options.opacity()));
        }
        if (!calculateFluidOnWorld().contains(viewport.visibleArea().expand(configuration.smokeCellSize() * configuration.smokeCellPadding() * 0.5))) {
            reassignGrid(viewport);
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
            final var origin = toCell(areaVelocityChange.area().origin());
            final var max = toCell(areaVelocityChange.area().bottomRight());
            for (int x = origin.x(); x < max.x(); x++) {
                for (int y = origin.y(); y < max.y(); y++) {
                    simulation.addVelocity(x, y, areaVelocityChange.velocity());
                }
            }
        }
    }

    public void applyObstacles(final List<Bounds> obstacles) {
        simulation.clearObstacles();

        for (final var obstacle : obstacles) {
            final var origin = toCell(obstacle.origin());
            final var max = toCell(obstacle.bottomRight());
            for (int x = origin.x(); x < max.x(); x++) {
                for (int y = origin.y(); y < max.y(); y++) {
                    simulation.setObstacle(x, y);
                }
            }
        }
    }

    public void adaptToViewport(final Viewport viewport) {
        if (isNull(simulation)) {
            reassignGrid(viewport);
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
        final int startCellX = Math.clamp((int) Math.floor(gridMinX / configuration.smokeCellSize()) - 1L, 0, maxCells - 1);
        final int startCellY = Math.clamp((int) Math.floor(gridMinY / configuration.smokeCellSize()) - 1L, 0, maxCells - 1);
        final int endCellX = Math.clamp((int) Math.ceil(gridMaxX / configuration.smokeCellSize()) + 1L, startCellX + 1, maxCells);
        final int endCellY = Math.clamp((int) Math.ceil(gridMaxY / configuration.smokeCellSize()) + 1L, startCellY + 1, maxCells);

        final Size size = Size.of(endCellX - startCellX, endCellY - startCellY);
        return new ScreenBounds(Offset.origin().add(startCellX, startCellY), size);
    }

    private Bounds calculateFluidOnWorld() {
        return Bounds.atOrigin(worldAnchor, (double) configuration.smokeCellSize() * simulation.resolution(), (double) configuration.smokeCellSize() * simulation.resolution());
    }

    private Bounds calculateBestBounds(final Viewport viewport) {
        final var bestBounds = viewport.visibleArea().expand((double) configuration.smokeCellPadding() * configuration.smokeCellSize()).snapExpand(configuration.smokeCellSize());
        return bestBounds.resize(
            Math.max(bestBounds.width(), bestBounds.height()),
            Math.max(bestBounds.width(), bestBounds.height()));
    }

    private void reassignGrid(final Viewport viewport) {
        awaitSimulationStep();
        final var lastAnchor = worldAnchor;
        final var boundsArea = calculateBestBounds(viewport);
        final long snappedX = Math.round(boundsArea.origin().x() / configuration.smokeCellSize()) * configuration.smokeCellSize();
        final long snappedY = Math.round(boundsArea.origin().y() / configuration.smokeCellSize()) * configuration.smokeCellSize();
        worldAnchor = Vector.of(snappedX, snappedY);

        var oldSimulation = simulation;
        final int resolution = (int) Math.round(boundsArea.width() / configuration.smokeCellSize());
        simulation = new FluidSimulation(resolution);
        if (nonNull(lastAnchor)) {
            final int deltaX = (int) Math.round((worldAnchor.x() - lastAnchor.x()) / configuration.smokeCellSize());
            final int deltaY = (int) Math.round((worldAnchor.y() - lastAnchor.y()) / configuration.smokeCellSize());

            if (nonNull(oldSimulation)) {
                simulation.loadFrom(oldSimulation, deltaX, deltaY);
            }
        }
    }
}
