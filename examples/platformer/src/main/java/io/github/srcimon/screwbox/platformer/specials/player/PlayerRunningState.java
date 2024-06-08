package io.github.srcimon.screwbox.platformer.specials.player;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.platformer.components.GroundDetectorComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerControlComponent;

import java.io.Serial;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class PlayerRunningState implements EntityState {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/specials/player.json", "running");

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(RenderComponent.class).sprite = SPRITE.get();
        entity.get(ParticleEmitterComponent.class).isEnabled = true;
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.hasComponent(DeathEventComponent.class)) {
            return new PlayerDeathState();
        }

        var momentum = entity.get(PhysicsComponent.class).momentum;

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
        entity.get(ParticleEmitterComponent.class).isEnabled = false;
    }

}
