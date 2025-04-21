package dev.screwbox.platformer.specials.player;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.platformer.components.DeathEventComponent;
import dev.screwbox.platformer.components.PlayerControlComponent;

import java.io.Serial;

import static dev.screwbox.tiles.Tileset.spriteAssetFromJson;

public class PlayerStandingState implements EntityState {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/specials/player.json", "standing");
    private final Time started = Time.now();

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(RenderComponent.class).sprite = SPRITE.get();
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.hasComponent(DeathEventComponent.class)) {
            return new PlayerDeathState();
        }
        var momentum = entity.get(PhysicsComponent.class).momentum;

        var controlComponent = entity.get(PlayerControlComponent.class);
        if (controlComponent.jumpDownPressed) {
            return new PlayerFallThroughState();
        } else if (controlComponent.jumpPressed) {
            return new PlayerJumpingStartedState();
        }

        if (Math.abs(momentum.x()) > 5) {
            return new PlayerRunningState();
        }

        if (Duration.since(started).isAtLeast(Duration.ofSeconds(5))) {
            return new PlayerIdleState();
        }
        return this;
    }

}
