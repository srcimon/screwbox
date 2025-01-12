package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;

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

    public AirFrictionComponent(double friction) {
        this(friction, friction);
    }

    public AirFrictionComponent(double frictionX, double frictionY) {
        this.frictionX = frictionX;
        this.frictionY = frictionY;
    }
}
