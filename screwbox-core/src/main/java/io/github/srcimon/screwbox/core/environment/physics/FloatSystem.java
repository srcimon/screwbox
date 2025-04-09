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
        final var delta = engine.loop().delta();
        final var floatings = engine.environment().fetchAll(FLOATINGS);
        final var antiGravity = engine.environment().tryFetchSingletonComponent(GravityComponent.class)
                .map(gravityComponent -> gravityComponent.gravity.multiply(delta).invert())
                .orElse(Vector.zero());

        for (final var fluidEntity : engine.environment().fetchAll(FLUIDS)) {
            final FluidComponent fluid = fluidEntity.get(FluidComponent.class);
            for (final var floating : floatings) {
                updateFloatingEntity(delta, fluidEntity, floating, fluid, antiGravity);
            }
        }
    }

    private void updateFloatingEntity(final double delta, final Entity fluidEntity, final Entity floating,final FluidComponent fluid, final Vector antiGravity) {
        final var options = floating.get(FloatComponent.class);
        if (!floatingIsWithinBounds(floating.position(), fluidEntity.bounds())) {
            options.attachedWave = null;
            return;
        }
        final double gap = fluidEntity.bounds().width() / (fluid.nodeCount - 1);
        final double xRelative = floating.position().x() - fluidEntity.origin().x();
        final int nodeNr = (int) (xRelative / gap);
        final double heightLeft = fluid.height[nodeNr];
        final double heightRight = fluid.height[nodeNr + 1];
        final double height = fluidEntity.bounds().minY() - floating.position().y() + (heightLeft + heightRight) / 2.0;

        if (height < 0) {
            final var physics = floating.get(PhysicsComponent.class);
            physics.momentum = physics.momentum
                    .addY(delta * -options.buoyancy)
                    .add(antiGravity)
                    .add(calculateFriction(delta * options.friction, physics));
        }
        final double waveAttachmentDistance = floating.bounds().height() / 2.0;
        options.attachedWave = height > -waveAttachmentDistance && height < waveAttachmentDistance
                ? Line.between(fluidEntity.origin().add(nodeNr * gap, heightLeft), fluidEntity.origin().add((nodeNr + 1) * gap, heightRight))
                : null;
    }

    private static boolean floatingIsWithinBounds(final Vector floating, final Bounds fluid) {
        return floating.x() >= fluid.minX()
                && floating.x() <= fluid.maxX()
                && fluid.maxY() >= floating.y();
    }

    private static Vector calculateFriction(final double friction, final PhysicsComponent physics) {
        final double x = physics.momentum.x();
        final double y = physics.momentum.y();
        return Vector.of(
                Math.clamp(modifier(x) * friction * -1, -Math.abs(x), Math.abs(x)),
                Math.clamp(modifier(y) * friction * -1, -Math.abs(y), Math.abs(y)));
    }

}
