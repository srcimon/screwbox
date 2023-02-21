package io.github.simonbas.screwbox.examples.platformer.specials.player;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.assets.Asset;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntityState;
import io.github.simonbas.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.simonbas.screwbox.core.entities.components.RenderComponent;
import io.github.simonbas.screwbox.core.graphics.Sprite;
import io.github.simonbas.screwbox.examples.platformer.components.DeathEventComponent;
import io.github.simonbas.screwbox.examples.platformer.components.GroundDetectorComponent;
import io.github.simonbas.screwbox.examples.platformer.components.PlayerControlComponent;
import io.github.simonbas.screwbox.examples.platformer.components.SmokeEmitterComponent;

import static io.github.simonbas.screwbox.tiled.Tileset.spriteAssetFromJson;

public class PlayerRunningState implements EntityState {

    private static final long serialVersionUID = 1L;

    private static final Asset<Sprite> SPRITE = spriteAssetFromJson("tilesets/specials/player.json", "running");

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(RenderComponent.class).sprite = SPRITE.get();
        entity.add(new SmokeEmitterComponent());
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.hasComponent(DeathEventComponent.class)) {
            return new PlayerDeathState();
        }

        var momentum = entity.get(PhysicsBodyComponent.class).momentum;

        var controlComponent = entity.get(PlayerControlComponent.class);
        if (controlComponent.jumpPressed) {
            return new PlayerJumpingState();
        }

        if (!entity.get(GroundDetectorComponent.class).isOnGround) {
            return new PlayerFallingState();
        }

        if (Math.abs(momentum.x()) > 5) {
            return this;
        }
        return new PlayerStandingState();
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.remove(SmokeEmitterComponent.class);
    }

}
