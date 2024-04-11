package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class ParticleAnimateOpacityComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Percent from;
    public Percent to;

    public ParticleAnimateOpacityComponent() {
        this(Percent.zero(), Percent.max());
    }

    public ParticleAnimateOpacityComponent(final Percent from, final Percent to) {
        this.from = from;
        this.to = to;
    }
}
