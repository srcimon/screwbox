package dev.screwbox.core.environment.particles;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class ParticleInteractionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double range;
    public Percent modifier;
    public Vector lastPos;

    public ParticleInteractionComponent(final double range) {
        this(range, Percent.half());
    }

    public ParticleInteractionComponent(final double range, final Percent modifier) {
       this.range = range;
       this.modifier = modifier;
    }
}
