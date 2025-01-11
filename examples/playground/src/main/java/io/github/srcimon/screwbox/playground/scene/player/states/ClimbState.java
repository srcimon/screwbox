package io.github.srcimon.screwbox.playground.scene.player.states;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.playground.movement.MovementControlComponent;

public class ClimbState implements EntityState {

    @Override
    public void enter(Entity entity, Engine engine) {
        entity.get(MovementControlComponent.class).isEnabled = false;
        PhysicsComponent physics = entity.get(PhysicsComponent.class);
        physics.gravityModifier = 0;
        physics.momentum = Vector.zero();
    }

    @Override
    public EntityState update(Entity entity, Engine engine) {
        return this;
    }

    @Override
    public void exit(Entity entity, Engine engine) {
        entity.get(PhysicsComponent.class).gravityModifier = 1;
    }
}
