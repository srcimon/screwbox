package io.github.srcimon.screwbox.examples.platformer.specials.player;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.ecosphere.Entity;
import io.github.srcimon.screwbox.core.ecosphere.EntityState;
import io.github.srcimon.screwbox.core.ecosphere.components.PhysicsBodyComponent;
import io.github.srcimon.screwbox.core.ecosphere.components.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.examples.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.examples.platformer.components.GroundDetectorComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerControlComponent;
import io.github.srcimon.screwbox.examples.platformer.components.SmokeEmitterComponent;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class PlayerRunningState implements EntityState {

    private static final long serialVersionUID = 1L;

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/specials/player.json", "running");

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(RenderComponent.class).sprite = SPRITE.get();
        entity.add(new SmokeEmitterComponent());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.hasComponent(DeathEventComponent.class)) {
            return new PlayerDeathState();
        }

        var momentum = entity.get(PhysicsBodyComponent.class).momentum;

        var controlComponent = entity.get(PlayerControlComponent.class);
        if (controlComponent.jumpPressed) {
            return new PlayerJumpingState();
        }

        if (!entity.get(GroundDetectorComponent.class).isOnGround) {
            return new PlayerFallingState();
        }

        if (Math.abs(momentum.x()) > 5) {
            return this;
        }
        return new PlayerStandingState();
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.remove(SmokeEmitterComponent.class);
    }

}
