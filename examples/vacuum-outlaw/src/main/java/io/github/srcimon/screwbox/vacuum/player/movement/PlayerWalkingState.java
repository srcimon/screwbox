package io.github.srcimon.screwbox.vacuum.player.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.vacuum.deathpit.FallenIntoDeathpitComponent;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class PlayerWalkingState implements EntityState {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/objects/player.json", "idle");

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(RenderComponent.class).sprite = SPRITE.get();
        entity.add(new MovementControlComponent());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.hasComponent(FallenIntoDeathpitComponent.class)) {
            return new FallToDeathState();
        }
        return entity.hasComponent(DashComponent.class)
                ? new PlayerDashingState()
                : this;
    }
}
