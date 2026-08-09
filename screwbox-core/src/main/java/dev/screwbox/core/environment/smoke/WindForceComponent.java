package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.smoke.Smoke;

import java.io.Serial;

/**
 * Physics {@link Entity entities} containing this component will be propelled by {@link Smoke} motion.
 *
 * @since 3.34.0
 */
public class WindForceComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Configures how fast the {@link Entity} captures the wind velocity.
     */
    public double adjustmentVelocity = 100;

}
