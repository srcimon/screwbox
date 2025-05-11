package dev.screwbox.core.environment.physics;

import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class FluidNoiseComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double strength;

    public FluidNoiseComponent() {
        this(100);
    }

    public FluidNoiseComponent(final double strength) {
        this.strength = strength;
    }
}
