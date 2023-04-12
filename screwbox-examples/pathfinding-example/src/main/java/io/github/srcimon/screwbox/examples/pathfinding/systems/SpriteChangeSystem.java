package io.github.srcimon.screwbox.examples.pathfinding.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.EntitySystem;
import io.github.srcimon.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.entities.components.RenderComponent;
import io.github.srcimon.screwbox.examples.pathfinding.components.SpriteChangeComponent;

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
