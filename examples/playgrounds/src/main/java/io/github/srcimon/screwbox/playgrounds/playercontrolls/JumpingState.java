package io.github.srcimon.screwbox.playgrounds.playercontrolls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

public class JumpingState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        final var physics = entity.get(PhysicsComponent.class);
        physics.momentum = physics.momentum.addY(-210);
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        if (entity.get(CollisionDetailsComponent.class).touchesBottom) {
            return new StandingState();
        }//TODO statecomponent.endorstState? // or pull that is jumping
        return this;
    }
}
