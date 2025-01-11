package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.playground.scene.player.states.ClimbState;

public class ClimbSystem implements EntitySystem {

    private static final Archetype GRABBERS = Archetype.ofSpacial(ClimbComponent.class, PhysicsComponent.class);

    @Override
    public void update(Engine engine) {
        for (final var entity : engine.environment().fetchAll(GRABBERS)) {
            var climbConfig = entity.get(ClimbComponent.class);
            if (engine.keyboard().isDown(climbConfig.grabKey)) {
                var collision = entity.get(CollisionDetailsComponent.class);
                if (collision.touchesLeft || collision.touchesRight) {
                    StateComponent stateComponent = entity.get(StateComponent.class);
                    if(!stateComponent.state.getClass().equals(StateComponent.class)) {
                        stateComponent.forcedState = new ClimbState();
                    }
                }
            }
        }
    }
}
