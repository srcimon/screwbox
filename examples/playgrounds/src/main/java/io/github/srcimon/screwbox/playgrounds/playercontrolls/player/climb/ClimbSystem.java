package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.climb;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
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
            final var climb = entity.get(ClimbComponent.class);
            final var collisionDetails = entity.get(CollisionDetailsComponent.class);
            boolean willGrabThisTime = wantsToGrab && (collisionDetails.touchesLeft || collisionDetails.touchesRight) && !climb.remainingTime.isNone() && climb.remainingTime.isAtLeast(Duration.ofNanos(1));
            if (willGrabThisTime) {
                if (!climb.isGrabbed) {
                    climb.grabStarted = engine.loop().time();
                }
            } else {
                if (climb.isGrabbed) {
                    climb.grabStarted = Time.unset();
                }
            }
            if (climb.lastGrabCheck.isUnset()) {
                climb.lastGrabCheck = engine.loop().time();
            }
            climb.remainingTime = Duration.ofNanos(Math.max(0, climb.remainingTime.nanos() - Duration.between(climb.lastGrabCheck, engine.loop().time()).nanos()));
            climb.isGrabbed = willGrabThisTime;
            climb.lastGrabCheck = engine.loop().time();
        }
    }
}
