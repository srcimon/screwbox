package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.climb;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.PlayerControls;

public class ClimbSystem implements EntitySystem {

    private static final Archetype CLIMBERS = Archetype.of(ClimbComponent.class);

    @Override
    public void update(Engine engine) {
        boolean wantsToGrab = engine.keyboard().isDown(PlayerControls.GRAB);
        for (final var entity : engine.environment().fetchAll(CLIMBERS)) {
            final var collisionDetails = entity.get(CollisionDetailsComponent.class);
            final var climb = entity.get(ClimbComponent.class);
            boolean nextGrabStatus = wantsToGrab && (collisionDetails.touchesLeft || collisionDetails.touchesRight) && !climb.remainingTime.isNone();
            if (nextGrabStatus && !climb.isGrabbed) {
                climb.grabStarted = engine.loop().time();
            }
            climb.isGrabbed = nextGrabStatus;
            if(climb.grabStarted.isSet()) {
                climb.remainingTime = Duration.ofNanos(Math.max(0, Duration.ofSeconds(2).nanos() - Duration.between(engine.loop().time(), climb.grabStarted).nanos()));
                if(climb.remainingTime.isNone()) {
                    climb.isGrabbed = false;
                }
            }
        }
    }
}
