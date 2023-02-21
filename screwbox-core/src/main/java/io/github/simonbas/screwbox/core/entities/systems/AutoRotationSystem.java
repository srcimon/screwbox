package io.github.simonbas.screwbox.core.entities.systems;

import io.github.simonbas.screwbox.core.Angle;
import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.Archetype;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntitySystem;
import io.github.simonbas.screwbox.core.entities.components.AutoRotationComponent;
import io.github.simonbas.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;

public class AutoRotationSystem implements EntitySystem {

    private static final Archetype ROTATING_BODIES = Archetype.of(
            PhysicsBodyComponent.class, RenderComponent.class, AutoRotationComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.entities().fetchAll(ROTATING_BODIES)) {
            final var physicsBody = entity.get(PhysicsBodyComponent.class);
            final var sprite = entity.get(RenderComponent.class);
            if (!physicsBody.momentum.isZero()) {
                sprite.rotation = Angle.ofMomentum(physicsBody.momentum);
            }
        }

    }

}
