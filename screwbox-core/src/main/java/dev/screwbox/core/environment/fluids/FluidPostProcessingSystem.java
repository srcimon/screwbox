package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.graphics.Offset;
import dev.screwbox.core.graphics.Viewport;
import dev.screwbox.core.graphics.internal.AwtMapper;
import dev.screwbox.core.graphics.postfilter.PostProcessingContext;
import dev.screwbox.core.graphics.postfilter.PostProcessingFilter;
import dev.screwbox.core.utils.Validate;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Processes all effects with {@link FluidPostProcessingComponent}.
 *
 * @since 3.28.0
 */
@ExecutionOrder(Order.PRESENTATION_EFFECTS)
public class FluidPostProcessingSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class, FluidPostProcessingComponent.class);

    private record FluidEffect(FluidComponent fluid, FluidPostProcessingComponent config) {

        FluidEffect {
            Validate.range(config.tileSize, 4, 32, "tile size must be in range 4 to 32");
        }
    }

    @Override
    public void update(final Engine engine) {
        final List<Entity> fluids = engine.environment().fetchAll(FLUIDS);
        if (fluids.isEmpty()) {
            return;
        }
        final List<FluidEffect> filterFluids = new ArrayList<>();
        for (final var fluid : fluids) {
            final var fluidComponent = fluid.get(FluidComponent.class);
            final Bounds boundingBox = Bounds.around(fluidComponent.outline.nodes());
            if (engine.graphics().isVisible(boundingBox)) {
                filterFluids.add(new FluidEffect(fluidComponent, fluid.get(FluidPostProcessingComponent.class)));
            }
        }
        if (!filterFluids.isEmpty()) {
            engine.graphics().postProcessing().addEffectFilter(new FluidPostFilter(filterFluids, engine.loop().runningTime()));
        }
    }

    private record FluidPostFilter(List<FluidEffect> effects, Duration runningTime) implements PostProcessingFilter {

        @Override
        public void apply(final Image source, final Graphics2D target, final PostProcessingContext context) {
            drawSourceImage(source, target, context);

            for (final var effect : effects) {
                final Bounds fluidBounds = Bounds.around(effect.fluid.outline.definitionNotes());
                if (fluidBounds.intersects(context.viewport().visibleArea())) {
                    renderFluid(source, target, context, effect);
                }
            }
        }

        private void renderFluid(final Image source, final Graphics2D target, final PostProcessingContext context, final FluidEffect effect) {
            final double index = (double) runningTime.milliseconds() / effect.config.interval.milliseconds();

            final Path2D outlinePath = AwtMapper.toPath(mapToViewport(context, effect.fluid.outline.definitionNotes()));
            final Rectangle bounds = outlinePath.getBounds();
            target.setClip(outlinePath);

            final var surfaceNodes = mapToViewport(context, effect.fluid.surface.definitionNotes());
            final double averageHeight = getAverageHeight(surfaceNodes);
            final int tileSize = Math.max(1, (int) (effect.config.tileSize * context.resolutionScale()));
            for (int y = (bounds.y / tileSize) * tileSize; y < bounds.y + bounds.height; y += tileSize) {
                for (int x = (bounds.x / tileSize) * tileSize; x < bounds.x + bounds.width; x += tileSize) {
                    final Offset position = Offset.at(x, y);
                    final Vector preciseOffset = calculatePreciseOffset(effect, position, index, context.viewport(), context, surfaceNodes, averageHeight);
                    final double damping = calculateDampening(position, tileSize, preciseOffset, bounds, surfaceNodes);

                    target.drawImage(source, x, y,
                        x + tileSize, y + tileSize,
                        x + (int) (preciseOffset.x() * damping),
                        y + (int) (preciseOffset.y() * damping),
                        x + (int) (preciseOffset.x() * damping) + tileSize,
                        y + (int) (preciseOffset.y() * damping) + tileSize, null);
                }
            }
        }

        private static double getAverageHeight(final List<Offset> nodes) {
            double sumY = 0.0;
            for (var offset : nodes) {
                sumY += offset.y();
            }
            return sumY / nodes.size();
        }

        private static List<Offset> mapToViewport(final PostProcessingContext context, final List<Vector> vectors) {
            final List<Offset> outlineNodes = new ArrayList<>();
            final Offset offset = context.viewport().canvas().offset();
            for (final var node : vectors) {
                outlineNodes.add(context.viewport().toCanvas(node).add(offset));
            }
            return outlineNodes;
        }

        // dampening prevents copying graphics outside of fluid shape
        private double calculateDampening(final Offset position, final int tileSize, final Vector off, final Rectangle bounds, final List<Offset> surfaceNodes) {
            double targetX = position.x() + off.x();
            double targetY = position.y() + off.y();

            if (targetX < bounds.x || targetX + tileSize > bounds.x + bounds.width || targetY + tileSize > bounds.y + bounds.height) {
                return 0.0;
            }
            final double preciseSurfaceY = getInterpolatedY(targetX + tileSize / 2.0, surfaceNodes, tileSize);
            return targetY < preciseSurfaceY
                ? Math.clamp(1.0 - (preciseSurfaceY - targetY) / 8.0, 0.0, 1.0)
                : 1.0;
        }

        private static double getInterpolatedY(double screenX, List<Offset> nodes, int tileSize) {
            final int leftIndex = Math.clamp((int) (screenX / tileSize), 0, nodes.size() - 1);
            final int rightIndex = Math.clamp(leftIndex + 1L, 0, nodes.size() - 1);
            final double transform = screenX / tileSize - leftIndex;
            return nodes.get(leftIndex).y() * (1.0 - transform) + nodes.get(rightIndex).y() * transform;
        }

        private static Vector calculatePreciseOffset(final FluidEffect effect, final Offset position, final double index, final Viewport viewport, final PostProcessingContext context, final List<Offset> surfaceNodes, final double avgY) {
            final Vector worldPos = viewport.toWorld(context.bounds().offset().add(position));
            final var node = surfaceNodes.get(Math.clamp(position.x() / effect.config.tileSize, 0, surfaceNodes.size() - 1));
            final double distToSurface = Math.abs((context.bounds().y() + position.y()) - node.y());
            final double decay = Math.max(0, 1.0 - (distToSurface / (context.height() * 0.8)));
            final double wave = Math.abs(node.y() - avgY) * effect.config.waveImpact.value() * 0.5 * decay;
            final double dx = (Math.sin(index * 0.5 + (worldPos.x() + worldPos.y()) * 0.05) * 3.0 + (Math.sin(index + worldPos.x() * 0.1) * wave)) * 16 / context.resolutionScale();
            final double dy = Math.cos(index * 0.7 + worldPos.y() * 0.1) * (5 / context.resolutionScale() + wave * 4);
            return Vector.of(dx * effect.config.horizontalDistortion.value(), dy * effect.config.verticalDistortion.value());
        }
    }
}
