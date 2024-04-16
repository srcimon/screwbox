package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class ParticleInteractionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double range;

    public ParticleInteractionComponent(final double range) {
        this.range = range;
    }
}
