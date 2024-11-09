package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

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

            if (physicsComponent.friction != 0 && !physicsComponent.momentum.isZero()) {
                final Vector momentum = physicsComponent.momentum;
                final var speedChange = momentum
                        .length(abs(physicsComponent.friction) * engine.loop().delta())
                        .multiply(modifier(physicsComponent.friction));

                final boolean frictionIsPositive = physicsComponent.friction > 0;
                final double nextX = calculateUpdateValue(frictionIsPositive, speedChange.x(), momentum.x());
                final double nextY = calculateUpdateValue(frictionIsPositive, speedChange.y(), momentum.y());

                physicsComponent.momentum = Vector.of(nextX, nextY);
            }
        }
    }

    private double calculateUpdateValue(final boolean frictionIsPositive, final double speedChange, final double momentum) {
        final boolean isSpeedUp = frictionIsPositive ? speedChange > 0 : speedChange < 0;
        return isSpeedUp
                ? max(0, momentum - speedChange)
                : min(0, momentum - speedChange);
    }
}
