package io.github.srcimon.screwbox.examples.platformer.specials.player;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.SoundBundle;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.examples.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerControlComponent;

import java.io.Serial;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

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
        physicsBodyComponent.momentum = Vector.of(physicsBodyComponent.momentum.x(), -180);
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
