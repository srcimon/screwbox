package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

/**
 * Applies jump on key press for all {@link Entity entities} having {@link JumpControlComponent}.
 *
 * @see JumpControlComponent
 * @since 2.15.0
 */
public class JumpControlSystem implements EntitySystem {

    private static final Archetype JUMPERS = Archetype.of(JumpControlComponent.class, PhysicsComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var jumper : engine.environment().fetchAll(JUMPERS)) {
            final var control = jumper.get(JumpControlComponent.class);
            if (engine.keyboard().isPressed(control.keyAlias)) {
                control.latestRequest = engine.loop().time();
            }
            if (control.isEnabled && control.gracePeriod.addTo(control.latestRequest).isAfter(engine.loop().time())) {
                final var physics = jumper.get(PhysicsComponent.class);
                physics.momentum = physics.momentum.replaceY(-control.acceleration);
                control.lastActivation = engine.loop().time();
            }
        }
    }
}

