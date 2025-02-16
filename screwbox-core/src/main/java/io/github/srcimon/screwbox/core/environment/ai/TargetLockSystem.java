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
            final var targetLock = entity.get(TargetLockComponent.class);
            engine.environment().tryFetchById(targetLock.targetId).ifPresent(target -> {
                final var render = entity.get(RenderComponent.class);
                final var rotation = render.options.rotation();
                final var targetRotation = Rotation.ofMovement(target.position().substract(entity.position()));
                final var wantedRotation = rotation.delta(targetRotation).degrees();
                final var deltaDegrees = Math.abs(wantedRotation) < 1 // snap rotation
                        ? wantedRotation
                        : wantedRotation * engine.loop().delta(targetLock.speed);
                render.options = render.options.rotation(rotation.addDegrees(deltaDegrees));
            });
        }
    }
}
