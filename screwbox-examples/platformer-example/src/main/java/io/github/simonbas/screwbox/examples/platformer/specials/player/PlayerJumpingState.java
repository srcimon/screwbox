package io.github.simonbas.screwbox.examples.platformer.specials.player;

import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.EntityState;
import io.github.simonbas.screwbox.examples.platformer.components.DeathEventComponent;
import io.github.simonbas.screwbox.examples.platformer.components.GroundDetectorComponent;
import io.github.simonbas.screwbox.examples.platformer.components.PlayerControlComponent;

public class PlayerJumpingState implements EntityState {

    private static final long serialVersionUID = 1L;

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(PlayerControlComponent.class).allowJumpPush = false;
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.hasComponent(DeathEventComponent.class)) {
            return new PlayerDeathState();
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
