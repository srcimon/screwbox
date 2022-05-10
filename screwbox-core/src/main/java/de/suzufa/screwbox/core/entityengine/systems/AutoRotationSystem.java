package de.suzufa.screwbox.core.entityengine.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.AutoRotationComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;

public class AutoRotationSystem implements EntitySystem {

    private static final Archetype ROTATING_BODIES = Archetype.of(
            PhysicsBodyComponent.class, SpriteComponent.class, AutoRotationComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final Entity entity : engine.entityEngine().fetchAll(ROTATING_BODIES)) {
            final var physicsBody = entity.get(PhysicsBodyComponent.class);
            final var sprite = entity.get(SpriteComponent.class);
            sprite.rotation = Rotation.ofMomentum(physicsBody.momentum);
        }

    }

}
