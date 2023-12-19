package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

public class RotateSpriteSystem implements EntitySystem {

    private static final Archetype ROTATING_BODIES = Archetype.of(
            PhysicsComponent.class, RenderComponent.class, RotateSpriteComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.environment().fetchAll(ROTATING_BODIES)) {
            final var physicsBody = entity.get(PhysicsComponent.class);
            final var sprite = entity.get(RenderComponent.class);
            if (!physicsBody.momentum.isZero()) {
                sprite.rotation = Rotation.ofMomentum(physicsBody.momentum);
            }
        }

    }

}
