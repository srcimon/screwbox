package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.playground.scene.player.states.DashState;

public class DashControlSystem implements EntitySystem {

    private static final Archetype DASHERS = Archetype.ofSpacial(DashControlComponent.class, PhysicsComponent.class);


    @Override
    public void update(Engine engine) {
        for (final var entity : engine.environment().fetchAll(DASHERS)) {
            var dash = entity.get(DashControlComponent.class);
            if(dash.isEnabled) {
                if(engine.keyboard().isDown(dash.dashKey) && dash.remainingDashes > 0) {
                    dash.remainingDashes = dash.remainingDashes-1;
                    Vector dashVector = getDash(dash, engine.keyboard());
                    if(!dashVector.isZero()) {
                        entity.get(StateComponent.class).forcedState = new DashState();
                        entity.get(PhysicsComponent.class).momentum = dashVector.multiply(dash.speed);
                    }
                }
            }
        }
    }

    private Vector getDash(DashControlComponent dash, Keyboard keyboard) {
        if(keyboard.isDown(dash.upKey)) {
            return Vector.of(0, -1);
        }
        if(keyboard.isDown(dash.leftKey)) {
            return Vector.x(-1);
        }
        if(keyboard.isDown(dash.rightKey)) {
            return Vector.x(1);
        }
        return Vector.zero();
    }
}
