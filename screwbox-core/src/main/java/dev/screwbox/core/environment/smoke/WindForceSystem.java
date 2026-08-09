package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

/**
 * Sets physics {@link Entity entities} containing the {@link WindForceComponent} in motion.
 *
 * @since 3.34.0
 */
public class WindForceSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.ofSpacial(PhysicsComponent.class, WindForceComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(PHYSICS)) {
            final var physics = entity.get(PhysicsComponent.class);
            final var config = entity.get(WindForceComponent.class);
            final var smokeVelocity = engine.graphics().smoke().velocityAt(entity.position());
            physics.velocity = physics.velocity.advance(smokeVelocity, engine.loop().delta(config.adjustmentVelocity));
        }
    }
    //TODO GridBackground Debug system
    //TODO wind visualization system
}
