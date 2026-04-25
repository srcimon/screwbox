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

            //TODO move effect inside component
            List<Offset> outlineNodes = mapToViewport(context, effect.fluid.outline.definitionNotes());
            Path2D outlinePath = AwtMapper.toPath(outlineNodes);
            Rectangle bounds = outlinePath.getBounds();

            target.setClip(outlinePath);
            var surfaceNodes = mapToViewport(context, effect.fluid.surface.definitionNotes());
            double avgY = surfaceNodes.stream().mapToDouble(Offset::y).average().orElse(0.0);

            for (int y = (bounds.y / effect.config.tileSize) * effect.config.tileSize; y < bounds.y + bounds.height; y += effect.config.tileSize) {
                for (int x = (bounds.x / effect.config.tileSize) * effect.config.tileSize; x < bounds.x + bounds.width; x += effect.config.tileSize) {

                    Vector off = calculatePreciseOffset(x, y, effect.config.tileSize, index, context.viewport(), context, surfaceNodes, avgY);

                    double damping = calculateDampening(effect.config.tileSize, x, off, y, bounds, surfaceNodes);

                    int sX = x + (int) (off.x() * damping);
                    int sY = y + (int) (off.y() * damping);

                    target.drawImage(source, x, y, x + effect.config.tileSize, y + effect.config.tileSize, sX, sY, sX + effect.config.tileSize, sY + effect.config.tileSize, null);
                }
            }
        }

        private static List<Offset> mapToViewport(PostProcessingContext context, List<Vector> vectors) {
            List<Offset> outlineNodes = new ArrayList<>();
            for (final var node : vectors) {
                outlineNodes.add(context.viewport().toCanvas(node).add(context.viewport().canvas().offset()));
            }
            return outlineNodes;
        }

        private double calculateDampening(int tileSize, int x, Vector off, int y, Rectangle bounds, List<Offset> surfaceNodes) {
            double damping = 1.0;
            double targetX = x + off.x();
            double targetY = y + off.y();


            if (targetX < bounds.x || targetX + tileSize > bounds.x + bounds.width || targetY + tileSize > bounds.y + bounds.height) {
                damping = 0.0;
            } else {
                final double preciseSurfaceY = getInterpolatedY(targetX + tileSize / 2.0, surfaceNodes, tileSize);
                if (targetY < preciseSurfaceY) {
                    damping = Math.clamp(1.0 - (preciseSurfaceY - targetY) / 8.0, 0.0, 1.0);
                }
            }
            return damping;
        }

        private static double getInterpolatedY(double screenX, List<Offset> nodes, int tSize) {
            double floatIdx = screenX / tSize;
            int idxA = Math.clamp((int) floatIdx, 0, nodes.size() - 1);
            int idxB = Math.clamp(idxA + 1, 0, nodes.size() - 1);

            double t = floatIdx - idxA;
            return nodes.get(idxA).y() * (1.0 - t) + nodes.get(idxB).y() * t;
        }

        private static Vector calculatePreciseOffset(int x, int y, int tSize, double time, Viewport viewport, PostProcessingContext context, List<Offset> surfaceNodes, double avgY) {
            Vector worldPos = viewport.toWorld(context.bounds().offset().add(x, y));
            int nodeIdx = Math.clamp(x / tSize, 0, surfaceNodes.size() - 1);
            var node = surfaceNodes.get(nodeIdx);

            double distToSurface = Math.abs((context.bounds().y() + y) - node.y());
            double decay = Math.max(0, 1.0 - (distToSurface / (context.height() * 0.8)));
            double wave = Math.abs(node.y() - avgY) * 0.15 * decay;

            double dx = (Math.sin(time * 0.5 + (worldPos.x() + worldPos.y()) * 0.05) * 3.0 + (Math.sin(time + worldPos.x() * 0.1) * wave)) * 8 * context.resolutionScale();
            double dy = Math.cos(time * 0.7 + worldPos.y() * 0.1) * (5 * context.resolutionScale() + wave * 10);
            return Vector.of(dx, dy);
        }
    }
}
