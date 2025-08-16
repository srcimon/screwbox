package dev.screwbox.core.environment.controls;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

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
                control.lastUnansweredRequest = engine.loop().time();
            }
            if (control.isEnabled && control.gracePeriod.addTo(control.lastUnansweredRequest).isAfter(engine.loop().time())) {
                final var physics = jumper.get(PhysicsComponent.class);
                physics.velocity = physics.velocity.replaceY(-control.acceleration);
                control.lastActivation = engine.loop().time();
                control.lastUnansweredRequest = Time.unset();
            }
        }
    }
}

