package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;

import java.util.UUID;

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

            if (jumpControl.lastActivation.isAfter(suspensionControl.lastJumpDetection)) {
                suspensionControl.lastJumpDetection = jumpControl.lastActivation;
                suspensionControl.remainingJumps--;
                System.out.println("JUMPED " + suspensionControl.remainingJumps + "  " + UUID.randomUUID());
            }
            if (lastBottomContact.isAfter(suspensionControl.lastGroundDetection) && lastBottomContact.isAfter(jumpControl.lastActivation)) {
                suspensionControl.lastGroundDetection = lastBottomContact;
                if (suspensionControl.maxJumps > suspensionControl.remainingJumps) {
                    suspensionControl.remainingJumps = suspensionControl.maxJumps;
                    System.out.println("RESET");
                }
            }
            //suspensionControl.gracePeriod.addTo(
            jumpControl.isEnabled = suspensionControl.remainingJumps > 0;
        }
    }
}
