package dev.screwbox.core.environment.ai;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import java.io.Serial;

/**
 * Adds a patrolling movement pattern to {@link Entity entities} also having a {@link PhysicsComponent}.
 *
 * @since 2.12.0
 */
public class PatrolMovementComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double speed;

    public PatrolMovementComponent(final double speed) {
        this.speed = speed;
    }
}
