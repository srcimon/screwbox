package io.github.srcimon.screwbox.examples.helloworld;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class ParticleInteractionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double range;
    public Percent modifier;
    public Vector lastPos;

    public ParticleInteractionComponent(final double range) {
        this(range, Percent.max());
    }

    public ParticleInteractionComponent(final double range, final Percent modifier) {
       this.range = range;
       this.modifier = modifier;
    }
}
