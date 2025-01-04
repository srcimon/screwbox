package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.move;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.keyboard.Keyboard;
import io.github.srcimon.screwbox.core.utils.MathUtil;
import io.github.srcimon.screwbox.playgrounds.playercontrolls.player.PlayerControls;

public class MovementSystem implements EntitySystem {

    private static final Archetype MOVERS = Archetype.of(PhysicsComponent.class, MovementComponent.class);

    @Override
    public void update(Engine engine) {
        final var x = getX(engine.keyboard());

        if (x != 0) {
            for (final var entity : engine.environment().fetchAll(MOVERS)) {
                final var physics = entity.get(PhysicsComponent.class);
                double maxSpeed = entity.get(MovementComponent.class).speed;
                final var newX = physics.momentum.x() + maxSpeed * engine.loop().delta();
                final var cappedX = MathUtil.modifier(newX) == 1.0 ? Math.min(newX, maxSpeed) : Math.max(newX, -maxSpeed); //TODO MathUtil funktion for cap
                physics.momentum = Vector.of(cappedX, physics.momentum.y());
            }
        }
    }

    private double getX(Keyboard keyboard) {
        if (keyboard.isDown(PlayerControls.LEFT)) {
            return -1;
        }
        return keyboard.isDown(PlayerControls.RIGHT) ? 1 : 0;
    }
}
