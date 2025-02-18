package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;

/**
 * Disables and re-enables jumping using the {@link JumpControlComponent} for all {@link Entity entities} having
 * a {@link SuspendJumpControlComponent} and {@link CollisionDetailsComponent}.
 *
 * @see SuspendJumpControlComponent
 * @see CollisionDetailsComponent
 * @since 2.15.0
 */
public class SuspendJumpControlSystem implements EntitySystem {

    private static final Archetype JUMPERS = Archetype.of(
            SuspendJumpControlComponent.class, JumpControlComponent.class, CollisionDetailsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(JUMPERS)) {
            final var jumpControl = entity.get(JumpControlComponent.class);
            final var suspensionControl = entity.get(SuspendJumpControlComponent.class);
            final var lastBottomContact = entity.get(CollisionDetailsComponent.class).lastBottomContact;

            // reduce remaining jumps on jump
            if (jumpControl.lastActivation.isAfter(suspensionControl.lastJumpDetection) ||(jumpControl.lastActivation.isSet() && suspensionControl.lastJumpDetection.isUnset())) {
                suspensionControl.lastJumpDetection = jumpControl.lastActivation;
                suspensionControl.remainingJumps--;

            }

            // reduce remaining jumps after loosing ground contact
            if (suspensionControl.remainingJumps == suspensionControl.maxJumps
                    && suspensionControl.gracePeriod.addTo(lastBottomContact).isBefore(engine.loop().time())) {
                suspensionControl.remainingJumps--;

            }

            // reset stats on ground contact
            if ((lastBottomContact.isAfter(suspensionControl.lastGroundDetection) || suspensionControl.lastJumpDetection.isUnset())
                    && (lastBottomContact.isAfter(jumpControl.lastActivation) || jumpControl.lastActivation.isUnset())) {
                suspensionControl.lastGroundDetection = lastBottomContact;
                suspensionControl.remainingJumps = suspensionControl.maxJumps;
            }
            jumpControl.isEnabled = suspensionControl.remainingJumps > 0;
        }
    }
}
