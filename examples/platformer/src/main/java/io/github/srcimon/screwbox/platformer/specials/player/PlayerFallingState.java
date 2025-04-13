package io.github.srcimon.screwbox.platformer.specials.player;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerControlComponent;

import static dev.screwbox.tiles.Tileset.spriteAssetFromJson;

public class PlayerFallingState implements EntityState {

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
