package dev.screwbox.core.environment.particles;

import dev.screwbox.core.Duration;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.core.particles.SpawnMode;
import dev.screwbox.core.utils.Scheduler;

import java.io.Serial;
import java.util.function.Supplier;

public class ParticleEmitterComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public boolean isEnabled = true;
    public SpawnMode spawnMode;
    public Scheduler scheduler;
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
        this.scheduler = Scheduler.withInterval(interval);
        this.spawnMode = spawnMode;
        this.particleOptions = particleOptions;
    }
}
