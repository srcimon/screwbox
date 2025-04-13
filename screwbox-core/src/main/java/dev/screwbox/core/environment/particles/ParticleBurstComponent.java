package dev.screwbox.core.environment.particles;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Component;

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
