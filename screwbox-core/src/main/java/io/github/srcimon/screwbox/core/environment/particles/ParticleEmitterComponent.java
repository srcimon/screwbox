package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import io.github.srcimon.screwbox.core.utils.Sheduler;

import java.io.Serial;
import java.util.function.Supplier;

public class ParticleEmitterComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public enum SpawnMode {
        POSITION,
        AREA
    }

    public boolean isEnabled = true;
    public SpawnMode spawnMode;
    public Sheduler sheduler;
    public ParticleOptions particleOptions;

    public ParticleEmitterComponent(final Duration interval, final Supplier<ParticleOptions> particleConfiguration) {
        this(interval, SpawnMode.AREA, particleConfiguration.get());
    }

    public ParticleEmitterComponent(final Duration interval, final ParticleOptions particleOptions) {
        this(interval, SpawnMode.AREA, particleOptions);
    }

    public ParticleEmitterComponent(final Duration interval, final SpawnMode spawnMode, final Supplier<ParticleOptions> particleConfiguration) {
        this(interval, spawnMode, particleConfiguration.get());
    }

    public ParticleEmitterComponent(final Duration interval, final SpawnMode spawnMode, final ParticleOptions particleOptions) {
        this.sheduler = Sheduler.withInterval(interval);
        this.spawnMode = spawnMode;
        this.particleOptions = particleOptions;
    }
}
