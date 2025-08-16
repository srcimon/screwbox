package dev.screwbox.core.environment.physics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Reduces {@link PhysicsComponent#velocity} of {@link Entity} over time.
 *
 * @since 2.11.0
 */
public class FrictionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double friction;

    /**
     * Create a new instance.
     *
     * @param friction friction applied on {@link Entity}.
     */
    public FrictionComponent(double friction) {
        this.friction = friction;
    }

}
