package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

public class MovementTargetSystem implements EntitySystem {

    public static final Archetype TARGETS = Archetype.of(PhysicsComponent.class, MovementTargetComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(TARGETS)) {
            final var target = entity.get(MovementTargetComponent.class);

        }
    }
}
