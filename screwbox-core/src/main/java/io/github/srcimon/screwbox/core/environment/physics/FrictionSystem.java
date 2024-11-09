package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;

/**
 * Applies friction on all {@link Entity entities} having a {@link PhysicsComponent}. Slows down entities by there specified
 * {@link PhysicsComponent#friction}. May also used to speed up when using negative values.
 */
public class FrictionSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.of(PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(PHYSICS)) {
            final var physicsComponent = entity.get(PhysicsComponent.class);

            final Vector momentum = physicsComponent.momentum;
            if (physicsComponent.friction != 0 && !momentum.isZero()) {
                final double xChange = -physicsComponent.friction * engine.loop().delta() * modifier(momentum.x());
                final double yChange = -physicsComponent.friction * engine.loop().delta() * modifier(momentum.y());

                final double nextX = xChange < 0
                        ? Math.max(0, momentum.x() + xChange)
                        : Math.min(0, momentum.x() + xChange);
                final double nextY = yChange < 0
                        ? Math.max(0, momentum.y() + yChange)
                        : Math.min(0, momentum.y() + yChange);

                physicsComponent.momentum = Vector.of(nextX, nextY);
            }
        }
    }
}
