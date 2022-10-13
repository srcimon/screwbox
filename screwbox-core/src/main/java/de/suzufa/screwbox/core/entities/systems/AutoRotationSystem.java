package de.suzufa.screwbox.core.entities.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Angle;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.components.AutoRotationComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.SpriteComponent;

public class AutoRotationSystem implements EntitySystem {

    private static final Archetype ROTATING_BODIES = Archetype.of(
            PhysicsBodyComponent.class, SpriteComponent.class, AutoRotationComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.entities().fetchAll(ROTATING_BODIES)) {
            final var physicsBody = entity.get(PhysicsBodyComponent.class);
            final var sprite = entity.get(SpriteComponent.class);
            if (!physicsBody.momentum.isZero()) {
                sprite.rotation = Angle.ofMomentum(physicsBody.momentum);
            }
        }

    }

}
