package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.smoke.Smoke;

import java.io.Serial;

/**
 * Applies constant velocity to {@link Smoke} within the entity bounds.
 *
 * @see Smoke
 * @since 3.33.0
 */
public class WindComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Target velocity of the wind.
     */
    public Vector velocity;

    /**
     * Velocity of adjustment between smoke velocity and target velocity.
     *
     * @since 3.34.0
     */
    public double adjustmentSpeed = 1.0;

    /**
     * Specifies the velocity of the wind.
     */
    public WindComponent(final Vector velocity) {
        this.velocity = velocity;
    }
}
