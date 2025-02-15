package io.github.srcimon.screwbox.core.environment.ai;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.utils.ListUtil;

import static java.util.Objects.nonNull;

public class RotateTowardsTargetSystem implements EntitySystem {

    private static final Archetype ROTORS = Archetype.of(RotateTowardsTargetComponent.class);

    @Override
    public void update(final Engine engine) {
        for(final var entity : engine.environment().fetchAll(ROTORS)) {
            final var rotor = entity.get(RotateTowardsTargetComponent.class);
            engine.environment().tryFetchById(rotor.targetId).ifPresent(target -> {
                final var targetRotationDegrees = Rotation.ofMovement(target.position().substract(entity.position())).degrees();
                final var currentRotationDegrees = rotor.rotation.degrees();
                final var nextRotationDegrees = currentRotationDegrees + (targetRotationDegrees - currentRotationDegrees) * engine.loop().delta(rotor.speed);
                rotor.rotation = Rotation.degrees(nextRotationDegrees);
                if(rotor.updateSprite) {
                    var render = entity.get(RenderComponent.class);
                    if(nonNull(render)) {
                        render.options = render.options.rotation(rotor.rotation);
                    }
                }
            });
        }
    }
}
