package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

import java.util.List;

import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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
            final FluidComponent fluid = fluidEntity.get(FluidComponent.class);
            final var wave = findWave(floating.position(), fluidEntity.bounds(), fluid.surface);
            var depth = detectDepth(wave, floating.position(), fluidEntity.bounds());
            if (nonNull(depth) && depth < 0) {
                final var physics = floating.get(PhysicsComponent.class);
                physics.momentum = physics.momentum
                        .addY(delta * -options.buoyancy)
                        .add(antiGravity)
                        .add(calculateFriction(delta * options.horizontalFriction, delta * options.verticalFriction, physics));
            }
            if (nonNull(depth) && Math.abs(depth) < floating.bounds().height() / 2.0) {
                options.attachedWave = wave;
                return;
            }
        }
    }

    private Double detectDepth(final Line wave, final Vector floatingPosition, final Bounds fluid) {
        if (isNull(wave)) {
            return null;
        }
        final Vector surfaceAnchor = wave.intersectionPoint(Line.normal(floatingPosition, -fluid.height()));
        return isNull(surfaceAnchor) ? null : surfaceAnchor.y() - floatingPosition.y();
    }

    private Line findWave(final Vector position, final Bounds fluid, final Path surface) {
        boolean isOutOfBounds = !(position.x() >= fluid.minX() && position.x() <= fluid.maxX() && fluid.maxY() >= position.y());
        if (isOutOfBounds) {
            return null;
        }
        final double gap = fluid.width() / (surface.nodeCount() - 1);
        final double xRelative = position.x() - fluid.origin().x();
        final int nodeNr = (int) (xRelative / gap);
        return Line.between(surface.node(nodeNr), surface.node(nodeNr + 1));
    }

    private Vector calculateFriction(final double frictionX, final double frictionY, final PhysicsComponent physics) {
        final double x = physics.momentum.x();
        final double y = physics.momentum.y();
        return Vector.of(
                Math.clamp(modifier(x) * frictionX * -1, -Math.abs(x), Math.abs(x)),
                Math.clamp(modifier(y) * frictionY * -1, -Math.abs(y), Math.abs(y)));
    }

}
