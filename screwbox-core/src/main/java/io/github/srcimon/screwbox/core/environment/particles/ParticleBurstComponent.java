package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

/**
 * Used to automatically shutdown particle emitters after a timeout.
 *
 * @see ParticleBurstSystem
 * @see ParticleEmitterComponent
 */
public class ParticleBurstComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Duration timeout;
    public Time activeSince = Time.unset();

    public ParticleBurstComponent(final Duration timeout) {
        this.timeout = timeout;
    }
}
