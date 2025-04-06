package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;

/**
 * Applies floating to physics {@link Entity entities} that also contain {@link FloatComponent}.
 *
 * @since 2.19.0
 */
public class FloatSystem implements EntitySystem {

    private static final Archetype FLUIDS = Archetype.ofSpacial(FluidComponent.class);
    private static final Archetype FLOATINGS = Archetype.ofSpacial(FloatComponent.class, PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        final var floatings = engine.environment().fetchAll(FLOATINGS);
        final var gravity = engine.environment().tryFetchSingletonComponent(GravityComponent.class)
                .map(gravityComponent -> gravityComponent.gravity).orElse(Vector.zero());
        for (final var fluidEntity : engine.environment().fetchAll(FLUIDS)) {
            final FluidComponent fluid = fluidEntity.get(FluidComponent.class);
            for (final var floating : floatings) {
                final Bounds fluidBounds = fluidEntity.bounds();
                final Bounds floatingBounds = floating.bounds();
                final var options = floating.get(FloatComponent.class);
                if (floatingIsWithinBounds(floatingBounds, fluidBounds)) {
                    final double gap = fluidBounds.width() / (fluid.nodeCount - 1);
                    final double xRelative = floatingBounds.position().x() - fluidBounds.origin().x();
                    final int nodeNr = (int) (xRelative / gap);
                    final double heightLeft = fluid.height[nodeNr];
                    final double heightRight = fluid.height[Math.min(nodeNr + 1, fluid.nodeCount-1)];
                    final double height = fluidBounds.minY() - floatingBounds.position().y() + (heightLeft + heightRight) / 2.0;


                    if (height < 0) {
                        final var physics = floating.get(PhysicsComponent.class);
                        physics.momentum = physics.momentum
                                .addY(engine.loop().delta() * -options.buoyancy)
                                .add(gravity.multiply(engine.loop().delta()).invert())
                                .add(calculateFriction(engine.loop().delta(), options, physics));
                    }
                    final double waveAttachmentDistance = floating.bounds().height() / 2.0;
                    options.attachedWave = height > -waveAttachmentDistance  && height < waveAttachmentDistance
                            ? Line.between(fluidBounds.origin().add(nodeNr * gap, heightLeft), fluidBounds.origin().add((nodeNr+1) * gap, heightRight))
                            : null;
                } else {
                    options.attachedWave = null;
                }
            }
        }
    }

    private static boolean floatingIsWithinBounds(final Bounds floating, final Bounds fluid) {
        return floating.maxX() > fluid.minX()
                && floating.minX() < fluid.maxX()
                && fluid.maxY() > floating.minY();
    }

    private static Vector calculateFriction(double delta, FloatComponent floatOptions, PhysicsComponent physics) {
        final double friction = floatOptions.friction * delta;
        final double x = physics.momentum.x();
        final double y = physics.momentum.y();
        return Vector.of(
                Math.clamp(modifier(x) * friction * -1, -Math.abs(x), Math.abs(x)),
                Math.clamp(modifier(y) * friction * -1, -Math.abs(y), Math.abs(y)));
    }

}
