package io.github.srcimon.screwbox.platformer.specials.player;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.platformer.archivements.JumpTwentyTimesArchivement;
import io.github.srcimon.screwbox.platformer.components.DeathEventComponent;
import io.github.srcimon.screwbox.platformer.components.GroundDetectorComponent;
import io.github.srcimon.screwbox.platformer.components.PlayerControlComponent;

public class PlayerJumpingState implements EntityState {

    private static final long serialVersionUID = 1L;

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(PlayerControlComponent.class).allowJumpPush = false;
        engine.archivements().progess(JumpTwentyTimesArchivement.class);
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
