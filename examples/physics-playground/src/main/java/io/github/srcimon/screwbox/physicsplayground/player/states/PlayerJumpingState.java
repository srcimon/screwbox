package io.github.srcimon.screwbox.physicsplayground.player.states;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.assets.Asset;
import io.github.srcimon.screwbox.core.audio.Sound;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.physicsplayground.player.PlayerInfoComponent;
import io.github.srcimon.screwbox.physicsplayground.tiles.Material;

public class PlayerJumpingState implements EntityState {

    private static final Asset<Sound> SOUND = Sound.assetFromFile("player/jump.wav");

    @Override
    public void enter(Entity entity, Engine engine) {
        engine.audio().playSound(SOUND);
        final var physicsComponent = entity.get(PhysicsComponent.class);
        physicsComponent.momentum = physicsComponent.momentum.addY(-200);
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if(entity.get(PlayerInfoComponent.class).currentGroundMaterial != Material.UNKNOWN) {
            return new PlayerStandingState();
        }
        return this;
    }
}
