package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.move;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.PlayerControls;

public class MovementSystem implements EntitySystem {

    private static final Archetype MOVERS = Archetype.of(PhysicsComponent.class, MovementComponent.class);

    @Override
    public void update(Engine engine) {
        final var direction = getX(engine.keyboard());
        for (final var entity : engine.environment().fetchAll(MOVERS)) {
            final var physics = entity.get(PhysicsComponent.class);
            final var movement = entity.get(MovementComponent.class);

            final double targetSpeed = direction * movement.maxSpeed;
            final double actualSpeed = physics.momentum.x();

            double newSpeed = actualSpeed + targetSpeed * engine.loop().delta() * movement.acceleration;
            if(targetSpeed == 0) {
                if(actualSpeed > 0) {
                    newSpeed = Math.max( actualSpeed - engine.loop().delta() * movement.acceleration *movement.maxSpeed, 0);
                } else if(actualSpeed < 0) {
                    newSpeed = Math.min( actualSpeed + engine.loop().delta() * movement.acceleration*movement.maxSpeed, 0);
                }
            }
            final double cappedNewSpeed = Math.clamp(newSpeed, -movement.maxSpeed, movement.maxSpeed);
            physics.momentum = Vector.of(cappedNewSpeed, physics.momentum.y());
        }
    }

    private double getX(Keyboard keyboard) {
        if (keyboard.isDown(PlayerControls.LEFT)) {
            return -1;
        }
        return keyboard.isDown(PlayerControls.RIGHT) ? 1 : 0;
    }
}
