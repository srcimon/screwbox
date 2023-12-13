package io.github.srcimon.screwbox.examples.platformer.specials.player;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.RigidBodyComponent;
import io.github.srcimon.screwbox.core.environment.components.RenderComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.examples.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.examples.platformer.components.PlayerControlComponent;

import static io.github.srcimon.screwbox.tiled.Tileset.spriteAssetFromJson;

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
        var momentum = entity.get(RigidBodyComponent.class).momentum;

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
