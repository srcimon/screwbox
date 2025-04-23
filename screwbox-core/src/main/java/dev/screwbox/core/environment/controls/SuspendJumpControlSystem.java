package dev.screwbox.core.environment.controls;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.CollisionDetailsComponent;
import dev.screwbox.core.environment.physics.FloatComponent;

import static java.util.Objects.nonNull;

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
            final var now = engine.loop().time();

            // reduce remaining jumps on jump
            if (isAfterOrSet(jumpControl.lastActivation, suspensionControl.lastJumpDetection)) {
                suspensionControl.lastJumpDetection = jumpControl.lastActivation;
                suspensionControl.remainingJumps--;
            }

            // reduce remaining jumps after loosing ground contact
            if (suspensionControl.remainingJumps == suspensionControl.maxJumps
                && suspensionControl.gracePeriod.addTo(lastBottomContact).isBefore(now)) {
                suspensionControl.remainingJumps--;
            }

            // reset stats on ground contact or float
            final boolean isOnGround = suspensionControl.gracePeriod.addTo(lastBottomContact).isAfter(now)
                                       && isAfterOrSet(lastBottomContact, jumpControl.lastActivation);

            if (isOnGround || suspensionControl.allowJumpWhileFloating && isFloating(entity)) {
                suspensionControl.remainingJumps = suspensionControl.maxJumps;
            }

            jumpControl.isEnabled = suspensionControl.remainingJumps > 0;
        }
    }

    private boolean isFloating(final Entity entity) {
        final var floatComponent = entity.get(FloatComponent.class);
        return nonNull(floatComponent) && nonNull(floatComponent.attachedWave);
    }

    private boolean isAfterOrSet(final Time time, final Time other) {
        return time.isAfter(other) || time.isSet() && other.isUnset();
    }
}
