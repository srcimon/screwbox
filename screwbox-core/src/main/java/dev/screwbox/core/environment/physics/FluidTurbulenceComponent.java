package dev.screwbox.core.environment.physics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Adds a turbulent motion to a fluid without need of physics {@link Entity} interaction.
 *
 * @since 3.2.0
 */
public class FluidTurbulenceComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double strength;

    public FluidTurbulenceComponent() {
        this(100);
    }

    public FluidTurbulenceComponent(final double strength) {
        this.strength = strength;
    }
}
