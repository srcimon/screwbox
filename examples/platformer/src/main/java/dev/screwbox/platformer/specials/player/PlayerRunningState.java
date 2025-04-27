package dev.screwbox.platformer.specials.player;

import dev.screwbox.core.Engine;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.particles.ParticleEmitterComponent;
import dev.screwbox.core.environment.physics.CollisionDetailsComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.platformer.components.DeathEventComponent;
import dev.screwbox.platformer.components.PlayerControlComponent;

import java.io.Serial;

import static dev.screwbox.tiled.Tileset.spriteAssetFromJson;

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

        if (!entity.get(CollisionDetailsComponent.class).touchesBottom) {
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
