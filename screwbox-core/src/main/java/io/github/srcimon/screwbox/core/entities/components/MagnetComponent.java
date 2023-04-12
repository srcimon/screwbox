package io.github.srcimon.screwbox.core.entities.components;

import io.github.srcimon.screwbox.core.entities.Component;

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
