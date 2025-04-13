package dev.screwbox.platformer.pathfinding.systems;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.platformer.pathfinding.components.SpriteChangeComponent;

public class SpriteChangeSystem implements EntitySystem {

    private static final Archetype DYNAMIC_SPRITES = Archetype.of(SpriteChangeComponent.class, RenderComponent.class);

    @Override
    public void update(Engine engine) {
        for (Entity entity : engine.environment().fetchAll(DYNAMIC_SPRITES)) {
            var standsStill = entity.get(PhysicsComponent.class).momentum.isZero();
            var sprite = entity.get(RenderComponent.class);
            var spriteChange = entity.get(SpriteChangeComponent.class);
            sprite.sprite = standsStill ? spriteChange.standing : spriteChange.walking;
        }
    }

}
