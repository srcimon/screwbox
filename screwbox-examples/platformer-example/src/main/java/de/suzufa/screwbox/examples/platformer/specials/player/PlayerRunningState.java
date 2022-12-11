package de.suzufa.screwbox.examples.platformer.specials.player;

import static de.suzufa.screwbox.tiled.Tileset.spriteAssetFromJson;

import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.assets.Asset;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntityState;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.RenderComponent;
import de.suzufa.screwbox.core.graphics.Sprite;
import de.suzufa.screwbox.examples.platformer.components.DeathEventComponent;
import de.suzufa.screwbox.examples.platformer.components.GroundDetectorComponent;
import de.suzufa.screwbox.examples.platformer.components.PlayerControlComponent;
import de.suzufa.screwbox.examples.platformer.components.SmokeEmitterComponent;

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
