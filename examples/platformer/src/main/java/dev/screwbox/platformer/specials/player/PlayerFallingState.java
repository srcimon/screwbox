package dev.screwbox.platformer.specials.player;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.physics.CollisionDetailsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.platformer.components.DeathEventComponent;
import dev.screwbox.platformer.components.PlayerControlComponent;

import java.io.Serial;

import static dev.screwbox.tiled.Tileset.spriteAssetFromJson;

public class PlayerFallingState implements EntityState {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Asset<Sprite> JUMPING = spriteAssetFromJson("tilesets/specials/player.json", "jumping");
    private final Time started = Time.now();

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(RenderComponent.class).sprite = JUMPING.get();
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.hasComponent(DeathEventComponent.class)) {
            return new PlayerDeathState();
        }

        final var control = entity.get(PlayerControlComponent.class);
        if (control.jumpPressed && Duration.since(started).isLessThan(Duration.ofMillis(200))) {
            return new PlayerJumpingStartedState();
        }
        if (control.digPressed) {
            return new PlayerDiggingState();
        }

        return entity.get(CollisionDetailsComponent.class).touchesBottom
                ? new PlayerStandingState()
                : this;
    }

}
