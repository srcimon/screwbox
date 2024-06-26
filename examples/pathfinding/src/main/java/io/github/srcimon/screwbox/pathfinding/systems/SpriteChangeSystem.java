package io.github.srcimon.screwbox.pathfinding.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.pathfinding.components.SpriteChangeComponent;

public class SpriteChangeSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.environment().fetchAllHaving(SpriteChangeComponent.class, RenderComponent.class)) {
            var standsStill = entity.get(PhysicsComponent.class).momentum.isZero();
            var sprite = entity.get(RenderComponent.class);
            var spriteChange = entity.get(SpriteChangeComponent.class);
            sprite.sprite = standsStill ? spriteChange.standing : spriteChange.walking;
        }
    }

}
