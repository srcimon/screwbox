package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

import java.util.List;

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

        final var fluids = engine.environment().fetchAll(FLUIDS);
        for (final var floating : floatings) {
            updateFloatingEntity(floating, fluids, delta, antiGravity);
        }
    }

    private void updateFloatingEntity(final Entity floating, final List<Entity> fluids, final double delta, final Vector antiGravity) {
        final var options = floating.get(FloatComponent.class);
        options.attachedWave = null;
        for (final var fluidEntity : fluids) {
            if (floatingIsWithinBounds(floating.position(), fluidEntity.bounds())) {
                final FluidComponent fluid = fluidEntity.get(FluidComponent.class);
                final Bounds fluidBounds = fluidEntity.bounds();
                final double gap = fluidBounds.width() / (fluid.nodeCount - 1);
                final double xRelative = floating.position().x() - fluidBounds.origin().x();
                final int nodeNr = (int) (xRelative / gap);
                final double heightLeft = fluid.height[nodeNr];
                final double heightRight = fluid.height[nodeNr + 1];
                final double height = fluidBounds.minY() - floating.position().y() + (heightLeft + heightRight) / 2.0;

                if (height < 0) {
                    final var physics = floating.get(PhysicsComponent.class);
                    physics.momentum = physics.momentum
                            .addY(delta * -options.buoyancy)
                            .add(antiGravity)
                            .add(calculateFriction(delta * options.horizontalFriction, delta * options.verticalFriction, physics));
                }
                final double waveAttachmentDistance = floating.bounds().height() / 2.0;
                options.attachedWave = height > -waveAttachmentDistance && height < waveAttachmentDistance
                        ? Line.between(fluidBounds.origin().add(nodeNr * gap, heightLeft), fluidBounds.origin().add((nodeNr + 1) * gap, heightRight))
                        : null;
            }
        }
    }

    private boolean floatingIsWithinBounds(final Vector floating, final Bounds fluid) {
        return floating.x() >= fluid.minX()
                && floating.x() <= fluid.maxX()
                && fluid.maxY() >= floating.y();
    }

    private Vector calculateFriction(final double frictionX, final double frictionY, final PhysicsComponent physics) {
        final double x = physics.momentum.x();
        final double y = physics.momentum.y();
        return Vector.of(
                Math.clamp(modifier(x) * frictionX * -1, -Math.abs(x), Math.abs(x)),
                Math.clamp(modifier(y) * frictionY * -1, -Math.abs(y), Math.abs(y)));
    }

}
