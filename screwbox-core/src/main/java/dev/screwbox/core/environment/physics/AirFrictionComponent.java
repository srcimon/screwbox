package dev.screwbox.core.environment.physics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Reduces {@link PhysicsComponent#momentum} of {@link Entity} over time.
 *
 * @since 2.11.0
 */
public class AirFrictionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double frictionX;
    public double frictionY;

    /**
     * Create a new instance using same x and y friction.
     *
     * @param friction friction to use for x and y
     */
    public AirFrictionComponent(double friction) {
        this(friction, friction);
    }

    /**
     * Create a new instance using distinct friction values for x and y.
     * @param frictionX friction to use for x
     * @param frictionY friction to use for y
     */
    public AirFrictionComponent(double frictionX, double frictionY) {
        this.frictionX = frictionX;
        this.frictionY = frictionY;
    }
}
