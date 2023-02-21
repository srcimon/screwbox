package io.github.simonbas.screwbox.examples.platformer.specials.player;

import io.github.simonbas.screwbox.core.Duration;
import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.Time;
import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntityState;
import io.github.simonbas.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.examples.platformer.components.DeathEventComponent;
import io.github.simonbas.screwbox.examples.platformer.components.PlayerControlComponent;

import static io.github.simonbas.screwbox.tiled.Tileset.spriteAssetFromJson;

public class PlayerStandingState implements EntityState {

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
        var momentum = entity.get(PhysicsBodyComponent.class).momentum;

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
