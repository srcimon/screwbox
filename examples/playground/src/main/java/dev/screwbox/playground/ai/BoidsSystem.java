package dev.screwbox.playground.ai;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

public class BoidsSystem implements EntitySystem {

    private static final Archetype BOIDS = Archetype.ofSpacial(PhysicsComponent.class, BoidsComponent.class);

    @Override
    public void update(Engine engine) {
        var boids = engine.environment().fetchAll(BOIDS);
        // 1. steer away from nearby boids (separation)
        // 2. steer in same direction as nearby boids (alignment)
        // 3. steer towards center of nearby boids (cohesion)
    }
}
