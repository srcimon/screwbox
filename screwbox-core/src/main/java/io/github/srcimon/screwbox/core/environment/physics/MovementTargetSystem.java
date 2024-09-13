package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
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
            var destinationVector = target.position.substract(entity.position());

            double deltaAcceleration = engine.loop().delta(target.acceleration);
            Vector speedChange = destinationVector.length(deltaAcceleration);
            if(physics.momentum.length() > destinationVector.length() * 4.0) {
                speedChange = physics.momentum.invert().length(deltaAcceleration);
            }
            Vector newMomentum = physics.momentum.add(speedChange);
            physics.momentum = newMomentum.length(Math.min(newMomentum.length(), target.maxSpeed));
            engine.graphics().world()
                    .drawLine(entity.position(), entity.position().add(destinationVector), LineDrawOptions.color(Color.RED).strokeWidth(1))
                    .drawLine(entity.position(), entity.position().add(physics.momentum), LineDrawOptions.color(Color.BLUE).strokeWidth(2))
                    .drawCircle(target.position, 4, CircleDrawOptions.filled(Color.YELLOW));
        }
    }
}
