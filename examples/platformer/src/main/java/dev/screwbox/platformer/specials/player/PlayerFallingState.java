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

import static dev.screwbox.tiles.Tileset.spriteAssetFromJson;

public class PlayerFallingState implements EntityState {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/specials/player.json", "jumping");

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

        PlayerControlComponent control = entity.get(PlayerControlComponent.class);
        if (control.jumpPressed && Duration.since(started).isLessThan(Duration.ofMillis(200))) {
            return new PlayerJumpingStartedState();
        }

        if (control.digPressed) {
            return new PlayerDiggingState();
        }

        if (entity.get(CollisionDetailsComponent.class).touchesBottom) {
            return new PlayerStandingState();
        }
        return this;
    }

}
