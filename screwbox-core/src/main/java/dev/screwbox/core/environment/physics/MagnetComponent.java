package dev.screwbox.core.environment.physics;

import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class MagnetComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double force;
    public double range;

    public MagnetComponent(final double force, final double range) {
        this.force = force;
        this.range = range;
    }

}
