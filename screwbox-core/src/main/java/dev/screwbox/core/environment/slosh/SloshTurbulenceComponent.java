package dev.screwbox.core.environment.slosh;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Adds a turbulent motion to a slosh volume without need of physics {@link Entity} interaction.
 *
 * @see <a href="https://screwbox.dev/docs/guides/slosh-physics/">Guide: Slosh phyics</a>
 * @since 3.2.0
 */
public class SloshTurbulenceComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double strength;

    public SloshTurbulenceComponent() {
        this(100);
    }

    public SloshTurbulenceComponent(final double strength) {
        this.strength = strength;
    }
}
