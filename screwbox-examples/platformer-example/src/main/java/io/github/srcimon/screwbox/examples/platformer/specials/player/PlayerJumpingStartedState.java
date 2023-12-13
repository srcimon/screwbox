package io.github.srcimon.screwbox.examples.platformer.specials.player;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.examples.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.examples.platformer.components.GroundDetectorComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerControlComponent;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

public class PlayerJumpingStartedState implements EntityState {

    private static final long serialVersionUID = 1L;
    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/specials/player.json", "jumping");
    private static final Asset<Sound> JUMP_SOUND = Sound.assetFromFile("sounds/jump.wav");

    private final Time started = Time.now();

    @Override
    public void enter(Entity entity, Engine engine) {
        engine.audio().playEffect(JUMP_SOUND.get());
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

        if (entity.get(GroundDetectorComponent.class).isOnGround) {
            return new PlayerStandingState();
        }
        return this;
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.get(PlayerControlComponent.class).allowJumpPush = false;
    }
}
