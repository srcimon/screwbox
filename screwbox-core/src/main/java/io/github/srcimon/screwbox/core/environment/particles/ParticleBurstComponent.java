package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class ParticleBurstComponent implements Component {


    @Serial
    private static final long serialVersionUID = 1L;

    public final Duration burstInterval;
    public final Duration idleInterval;
    public Time lastStateChange = Time.unset();

    public ParticleBurstComponent(final Duration burstInterval, final Duration idleInterval) {
        this.burstInterval = burstInterval;
        this.idleInterval = idleInterval;
    }
}
