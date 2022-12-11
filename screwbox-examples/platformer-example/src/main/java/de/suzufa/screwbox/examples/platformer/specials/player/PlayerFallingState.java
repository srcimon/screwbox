package de.suzufa.screwbox.examples.platformer.specials.player;

import static de.suzufa.screwbox.tiled.Tileset.spriteAssetFromJson;

import de.suzufa.screwbox.core.Duration;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Time;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntityState;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.examples.platformer.components.DeathEventComponent;
import de.suzufa.screwbox.examples.platformer.components.GroundDetectorComponent;
import de.suzufa.screwbox.examples.platformer.components.PlayerControlComponent;

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

        if (entity.get(GroundDetectorComponent.class).isOnGround) {
            return new PlayerStandingState();
        }
        return this;
    }

}
