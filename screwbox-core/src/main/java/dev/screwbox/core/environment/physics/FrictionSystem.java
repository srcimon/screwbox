package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;

import static dev.screwbox.core.utils.MathUtil.modifier;
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

            if (physicsComponent.friction != 0 && !physicsComponent.velocity.isZero()) {
                final Vector velocity = physicsComponent.velocity;
                final var speedChange = velocity
                        .length(abs(physicsComponent.friction) * engine.loop().delta())
                        .multiply(modifier(physicsComponent.friction));
//TODO check for vector.reduce
                final boolean frictionIsPositive = physicsComponent.friction > 0;
                final double nextX = calculateUpdateValue(frictionIsPositive, speedChange.x(), velocity.x());
                final double nextY = calculateUpdateValue(frictionIsPositive, speedChange.y(), velocity.y());

                physicsComponent.velocity = Vector.of(nextX, nextY);
            }
        }
    }

    private double calculateUpdateValue(final boolean frictionIsPositive, final double speedChange, final double velocity) {
        final boolean isSpeedUp = frictionIsPositive ? speedChange > 0 : speedChange < 0;
        return isSpeedUp
                ? max(0, velocity - speedChange)
                : min(0, velocity - speedChange);
    }
}
