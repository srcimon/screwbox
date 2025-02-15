package io.github.srcimon.screwbox.core.environment.ai;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;

/**
 * Rotates the sprites of all {@link Entity entities} having {@link TargetLockComponent} towards the
 * specified target {@link Entity}.
 *
 * @since 2.14.0
 */
public class TargetLockSystem implements EntitySystem {

    private static final Archetype ROTORS = Archetype.of(TargetLockComponent.class, RenderComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(ROTORS)) {
            final var rotor = entity.get(TargetLockComponent.class);
            engine.environment().tryFetchById(rotor.targetId).ifPresent(target -> {
                final var render = entity.get(RenderComponent.class);
                final var delta = target.position().substract(entity.position());
                var targetRotation = Rotation.ofMovement(delta);
                var currentRotation = render.options.rotation();

                var deltaRotation = currentRotation.delta(targetRotation);

                render.options = render.options.rotation(currentRotation.addDegrees(deltaRotation.degrees() * engine.loop().delta(rotor.speed)));
            });
        }
    }
}
