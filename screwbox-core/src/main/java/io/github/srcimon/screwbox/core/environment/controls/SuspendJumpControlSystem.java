package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Time;
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
            if (isAfterOrSet(jumpControl.lastActivation, suspensionControl.lastJumpDetection)) {
                suspensionControl.lastJumpDetection = jumpControl.lastActivation;
                suspensionControl.remainingJumps--;
            }

            // reduce remaining jumps after loosing ground contact
            if (suspensionControl.remainingJumps == suspensionControl.maxJumps
                    && suspensionControl.gracePeriod.addTo(lastBottomContact).isBefore(engine.loop().time())) {
                suspensionControl.remainingJumps--;
            }

            // reset stats on ground contact
            if (suspensionControl.gracePeriod.addTo(lastBottomContact).isAfter(engine.loop().time())
                    && isAfterOrSet(lastBottomContact, jumpControl.lastActivation)) {
                suspensionControl.remainingJumps = suspensionControl.maxJumps;
            }
            jumpControl.isEnabled = suspensionControl.remainingJumps > 0;
        }
    }

    private boolean isAfterOrSet(final Time time, final Time other) {
        return time.isAfter(other) || time.isSet() && other.isUnset();
    }
}
