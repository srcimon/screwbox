package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetailsComponent;

public class SuspendJumpSystem implements EntitySystem {

    private static final Archetype JUMPERS = Archetype.of(
            SuspendJumpComponent.class, JumpControlComponent.class, CollisionDetailsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(JUMPERS)) {
            final var jumpControl = entity.get(JumpControlComponent.class);
            final var gracePeriod = entity.get(SuspendJumpComponent.class).gracePeriod;
            final var lastBottomContact = entity.get(CollisionDetailsComponent.class).lastBottomContact;
            jumpControl.isEnabled = gracePeriod.addTo(lastBottomContact).isAfter(engine.loop().time());
        }
    }
}
