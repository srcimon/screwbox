package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class ParticleEmitterTimeoutComponent implements Component {


    @Serial
    private static final long serialVersionUID = 1L;

    public final Duration timeout;
    public Time lastActive = Time.unset();

    public ParticleEmitterTimeoutComponent(final Duration timeout) {
        this.timeout = timeout;
    }
}
