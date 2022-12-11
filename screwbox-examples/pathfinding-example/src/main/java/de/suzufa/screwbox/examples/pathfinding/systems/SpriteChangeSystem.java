package de.suzufa.screwbox.examples.pathfinding.systems;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.examples.pathfinding.components.SpriteChangeComponent;

public class SpriteChangeSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.entities().fetchAllHaving(SpriteChangeComponent.class, RenderComponent.class)) {
            var standsStill = entity.get(PhysicsBodyComponent.class).momentum.isZero();
            var sprite = entity.get(RenderComponent.class);
            var spriteChange = entity.get(SpriteChangeComponent.class);
            sprite.sprite = standsStill ? spriteChange.standing : spriteChange.walking;
        }
    }

}
