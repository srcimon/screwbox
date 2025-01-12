package io.github.srcimon.screwbox.playground.scene.player.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.playground.scene.player.states.DashState;

import static io.github.srcimon.screwbox.core.Vector.$;

public class DashControlSystem implements EntitySystem {

    private static final Archetype DASHERS = Archetype.ofSpacial(DashControlComponent.class, PhysicsComponent.class);


    @Override
    public void update(Engine engine) {
        for (final var entity : engine.environment().fetchAll(DASHERS)) {
            var dash = entity.get(DashControlComponent.class);
            if (dash.isEnabled && engine.keyboard().isDown(dash.dashKey) && dash.remainingDashes > 0) {
                dash.remainingDashes = dash.remainingDashes - 1;
                Vector dashVector = getDash(dash, engine.keyboard());
                if (!dashVector.isZero()) {
                    entity.get(StateComponent.class).forcedState = new DashState();
                    entity.get(PhysicsComponent.class).momentum = dashVector.multiply(dash.speed);
                }
            }
        }
    }

    private Vector getDash(DashControlComponent dash, Keyboard keyboard) {
        final double speedOrRightKey = keyboard.isDown(dash.rightKey) ? 1 : 0;
        final double x = keyboard.isDown(dash.leftKey) ? -1 : speedOrRightKey;
        final double y = keyboard.isDown(dash.upKey) ? -1 : 0;
        return $(x, y).length(1);
    }
}
