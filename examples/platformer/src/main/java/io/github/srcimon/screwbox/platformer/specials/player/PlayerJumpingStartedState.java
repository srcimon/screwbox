package io.github.srcimon.screwbox.platformer.specials.player;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.assets.Asset;
import dev.screwbox.core.audio.SoundBundle;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.logic.EntityState;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerControlComponent;

import java.io.Serial;

import static dev.screwbox.tiles.Tileset.spriteAssetFromJson;

public class PlayerJumpingStartedState implements EntityState {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/specials/player.json", "jumping");

    private final Time started = Time.now();

    @Override
    public void enter(Entity entity, Engine engine) {
        engine.audio().playSound(SoundBundle.JUMP);
        entity.get(RenderComponent.class).sprite = SPRITE.get();
        final var physicsBodyComponent = entity.get(PhysicsComponent.class);
        physicsBodyComponent.momentum = physicsBodyComponent.momentum.replaceY(-180);
        entity.get(PlayerControlComponent.class).allowJumpPush = true;
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.hasComponent(DeathEventComponent.class)) {
            return new PlayerDeathState();
        }

        if (Duration.since(started).isAtLeast(Duration.ofMillis(200))) {
            return new PlayerJumpingState();
        }

        if (entity.get(PlayerControlComponent.class).digPressed) {
            return new PlayerDiggingState();
        }

        return this;
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.get(PlayerControlComponent.class).allowJumpPush = false;
    }
}
