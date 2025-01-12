package io.github.srcimon.screwbox.playground.scene.player.movement;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.playground.scene.player.states.ClimbState;

public class GrabSystem implements EntitySystem {

    private static final Archetype GRABBERS = Archetype.ofSpacial(GrabComponent.class, PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(GRABBERS)) {
            var grabConfig = entity.get(GrabComponent.class);
            if (grabConfig.isEnabled && engine.keyboard().isDown(grabConfig.grabKey)) {
                tryToGrab(entity, grabConfig);
            }
        }
    }

    private static void tryToGrab(final Entity entity, final GrabComponent grabConfig) {
        final var physics = entity.get(PhysicsComponent.class);
        final var collision = entity.get(CollisionDetailsComponent.class);
        final boolean isInGrabPosition = (collision.touchesLeft && physics.momentum.x() <= 0)
                || (physics.momentum.x() >= 0 && collision.touchesRight);
        if (isInGrabPosition && grabConfig.stamina > 0) {
            final var stateComponent = entity.get(StateComponent.class);
            stateComponent.forcedState = stateComponent.state.getClass().equals(ClimbState.class)
                    ? stateComponent.state
                    : new ClimbState(collision.touchesLeft);
        }
    }
}
