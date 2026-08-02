package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.smoke.Smoke;

import java.io.Serial;

//TODO test

/**
 * Applies constant velocity to {@link Smoke} within the entity bounds.
 *
 * @see Smoke
 * @since 3.33.0
 */
public class WindComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector velocity;

    /**
     * Specifies the velocity of the wind.
     */
    public WindComponent(final Vector velocity) {
        this.velocity = velocity;
    }
}
