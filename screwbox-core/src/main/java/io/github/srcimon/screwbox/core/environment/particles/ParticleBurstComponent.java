package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class ParticleBurstComponent implements Component {


    @Serial
    private static final long serialVersionUID = 1L;

    public final Duration burstInterval;
    public Time activeSince = Time.unset();

    public ParticleBurstComponent(final Duration burstInterval) {
        this.burstInterval = burstInterval;
    }
}
