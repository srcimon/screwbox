package io.github.srcimon.screwbox.core.environment.ai;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;

public class RotateTowardsTargetSystem implements EntitySystem {

    private static final Archetype ROTORS = Archetype.of(RotateTowardsTargetComponent.class, RenderComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(ROTORS)) {
            final var rotor = entity.get(RotateTowardsTargetComponent.class);
            engine.environment().tryFetchById(rotor.targetId).ifPresent(target -> {
                final var render = entity.get(RenderComponent.class);
                final var delta = target.position().substract(entity.position());
                render.options = render.options.rotation(Rotation.ofMovement(delta));
            });
        }
    }
}
