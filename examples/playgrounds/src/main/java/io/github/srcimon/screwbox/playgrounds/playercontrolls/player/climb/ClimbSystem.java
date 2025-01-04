package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.climb;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.PlayerControls;

public class ClimbSystem implements EntitySystem {

    private static final Archetype CLIMBERS = Archetype.of(ClimbComponent.class);

    @Override
    public void update(Engine engine) {
        if (engine.keyboard().isDown(PlayerControls.GRAB)) {
            for (final var entity : engine.environment().fetchAll(CLIMBERS)) {

                final var collisionDetails = entity.get(CollisionDetailsComponent.class);
                final var climb = entity.get(ClimbComponent.class);
                if (climb.started.isUnset() && (collisionDetails.touchesLeft || collisionDetails.touchesRight)) {
                    climb.started = engine.loop().time();
                }
            }
        }
    }
}
