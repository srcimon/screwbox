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
            final var gracePeriod = entity.get(SuspendJumpControlComponent.class).gracePeriod;
            final var lastBottomContact = entity.get(CollisionDetailsComponent.class).lastBottomContact;
            jumpControl.isEnabled = gracePeriod.addTo(lastBottomContact).isAfter(engine.loop().time());
        }
    }
}
