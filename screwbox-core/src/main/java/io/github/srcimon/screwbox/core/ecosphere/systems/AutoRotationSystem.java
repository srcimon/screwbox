package io.github.srcimon.screwbox.core.ecosphere.systems;

import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.Archetype;
import io.github.srcimon.screwbox.core.ecosphere.Entity;
import io.github.srcimon.screwbox.core.ecosphere.EntitySystem;
import io.github.srcimon.screwbox.core.ecosphere.components.AutoRotationComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.RenderComponent;

public class AutoRotationSystem implements EntitySystem {

    private static final Archetype ROTATING_BODIES = Archetype.of(
            PhysicsBodyComponent.class, RenderComponent.class, AutoRotationComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.ecosphere().fetchAll(ROTATING_BODIES)) {
            final var physicsBody = entity.get(PhysicsBodyComponent.class);
            final var sprite = entity.get(RenderComponent.class);
            if (!physicsBody.momentum.isZero()) {
                sprite.rotation = Rotation.ofMomentum(physicsBody.momentum);
            }
        }

    }

}
