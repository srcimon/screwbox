package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Rotation;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

public class MovementRotationSystem implements EntitySystem {

    private static final Archetype ROTATING_BODIES = Archetype.of(
            PhysicsComponent.class, RenderComponent.class, MovementRotationComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.environment().fetchAll(ROTATING_BODIES)) {
            final var physicsBody = entity.get(PhysicsComponent.class);
            final var sprite = entity.get(RenderComponent.class);
            if (!physicsBody.momentum.isZero()) {
                sprite.options = sprite.options.rotation(Rotation.ofVector(physicsBody.momentum));
            }
        }

    }

}
