package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

import static io.github.srcimon.screwbox.core.utils.MathUtil.modifier;
import static java.lang.Math.abs;

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
                final var speedChange = momentum
                        .length(abs(physicsComponent.friction) * engine.loop().delta())
                        .multiply(modifier(physicsComponent.friction));
                if(physicsComponent.friction > 0) {
                    final double nextX = speedChange.x() > 0
                            ? Math.max(0, momentum.x() - speedChange.x())
                            : Math.min(0, momentum.x() - speedChange.x());
                    final double nextY = speedChange.y() > 0
                            ? Math.max(0, momentum.y() - speedChange.y())
                            : Math.min(0, momentum.y() - speedChange.y());

                    physicsComponent.momentum = Vector.of(nextX, nextY);
                } else {
                    final double nextX = speedChange.x() < 0
                            ? Math.max(0, momentum.x() - speedChange.x())
                            : Math.min(0, momentum.x() - speedChange.x());
                    final double nextY = speedChange.y() < 0
                            ? Math.max(0, momentum.y() - speedChange.y())
                            : Math.min(0, momentum.y() - speedChange.y());
                    physicsComponent.momentum = Vector.of(nextX, nextY);
                }
            }
        }
    }
}
