package io.github.srcimon.screwbox.vacuum.player.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.vacuum.deathpit.DeathpitVictimComponent;
import io.github.srcimon.screwbox.vacuum.deathpit.FallenIntoDeathpitComponent;
import io.github.srcimon.screwbox.vacuum.player.attack.PlayerAttackControl;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class PlayerWalkingState implements EntityState {

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/objects/player.json", "idle");

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(RenderComponent.class).sprite = SPRITE.get();
        entity.addIfNotPresent(new DeathpitVictimComponent());
        entity.addIfNotPresent(new MovementControlComponent());
        entity.addIfNotPresent(new PlayerAttackControl());
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

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.remove(DeathpitVictimComponent.class);
    }
}
