package de.suzufa.screwbox.examples.platformer.specials.player;

import static de.suzufa.screwbox.tiled.Tileset.spriteAssetFromJson;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.audio.Sound;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntityState;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.examples.platformer.components.DeathEventComponent;
import de.suzufa.screwbox.examples.platformer.components.GroundDetectorComponent;
import de.suzufa.screwbox.examples.platformer.components.PlayerControlComponent;

public class PlayerJumpingStartedState implements EntityState {

    private static final long serialVersionUID = 1L;
    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/specials/player.json", "jumping");
    private static final Asset<Sound> JUMP_SOUND = Sound.assetFromFile("sounds/jump.wav");

    private final Time started = Time.now();

    @Override
    public void enter(Entity entity, Engine engine) {
        engine.audio().playEffect(JUMP_SOUND.get());
        entity.get(RenderComponent.class).sprite = SPRITE.get();
        final var physicsBodyComponent = entity.get(PhysicsBodyComponent.class);
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
