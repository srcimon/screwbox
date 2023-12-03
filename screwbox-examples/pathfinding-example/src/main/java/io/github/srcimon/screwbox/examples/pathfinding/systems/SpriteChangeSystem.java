package io.github.srcimon.screwbox.examples.pathfinding.systems;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ecosphere.Entity;
import io.github.srcimon.screwbox.core.ecosphere.EntitySystem;
import io.github.srcimon.screwbox.core.ecosphere.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.RenderComponent;
import io.github.srcimon.screwbox.examples.pathfinding.components.SpriteChangeComponent;

public class SpriteChangeSystem implements EntitySystem {

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.ecosphere().fetchAllHaving(SpriteChangeComponent.class, RenderComponent.class)) {
            var standsStill = entity.get(PhysicsBodyComponent.class).momentum.isZero();
            var sprite = entity.get(RenderComponent.class);
            var spriteChange = entity.get(SpriteChangeComponent.class);
            sprite.sprite = standsStill ? spriteChange.standing : spriteChange.walking;
        }
    }

}
