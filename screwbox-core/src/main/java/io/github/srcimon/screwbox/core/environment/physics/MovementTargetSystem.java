package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.graphics.CircleDrawOptions;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.LineDrawOptions;

public class MovementTargetSystem implements EntitySystem {

    public static final Archetype TARGETS = Archetype.of(TransformComponent.class, PhysicsComponent.class, MovementTargetComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var entity : engine.environment().fetchAll(TARGETS)) {
            final var physics = entity.get(PhysicsComponent.class);
            final var target = entity.get(MovementTargetComponent.class);
            final var acceleration = target.position.substract(entity.position()).length(engine.loop().delta(target.acceleration));

            physics.momentum = physics.momentum.add(acceleration);
            double maxSpeed = Math.min(physics.momentum.length(), target.maxSpeed);
            double desiredSpeed = Math.min(maxSpeed, entity.position().distanceTo(target.position));
            physics.momentum = physics.momentum.length(desiredSpeed);
            engine.graphics().world()
                    .drawLine(entity.position(), entity.position().add(physics.momentum), LineDrawOptions.color(Color.BLUE).strokeWidth(2))
                    .drawCircle(target.position, 4, CircleDrawOptions.filled(Color.YELLOW));
        }
    }
}
