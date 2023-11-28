package io.github.srcimon.screwbox.examples.particles;

import io.github.srcimon.screwbox.core.entities.Component;
import io.github.srcimon.screwbox.core.utils.Sheduler;

import java.io.Serial;

public class ParticleEmitterComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Sheduler sheduler = Sheduler.everySecond();

}
