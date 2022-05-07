package de.suzufa.screwbox.examples.light;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Rotation;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Archetype;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.EntitySystem;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entityengine.components.SpriteComponent;

public class AutoRotateSpriteSystem implements EntitySystem {

    private static final Archetype ROTATING_SPRITES = Archetype.of(PhysicsBodyComponent.class, SpriteComponent.class,
            AutoRotateSpriteComponent.class);

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.entityEngine().fetchAll(ROTATING_SPRITES)) {
            Vector momentum = entity.get(PhysicsBodyComponent.class).momentum;
            entity.get(SpriteComponent.class).rotation = Rotation.normalAngle(momentum);
        }

    }

}
