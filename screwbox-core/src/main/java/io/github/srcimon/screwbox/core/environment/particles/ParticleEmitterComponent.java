package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.particles.ParticleOptions;
import io.github.srcimon.screwbox.core.utils.Sheduler;

import java.io.Serial;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ParticleEmitterComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    //TODO Javadoc changelog and test
    public enum SpawnMode {
        POSITION(bounds -> Bounds.atPosition(bounds.position(), 0, 0)),
        AREA(bounds -> bounds),
        LEFT(bounds -> Bounds.atOrigin(bounds.origin(), 1, bounds.height())),
        RIGHT(bounds -> Bounds.atOrigin(bounds.origin().addX(bounds.width()-1), 1, bounds.height())),
        TOP(bounds -> Bounds.atOrigin(bounds.origin(), bounds.width(), 1)),
        BOTTOM(bounds -> Bounds.atOrigin(bounds.origin().addY(bounds.height() - 1), bounds.width(), 1));

        private final UnaryOperator<Bounds> spawnArea;

        SpawnMode( final UnaryOperator<Bounds> spawnArea) {
            this.spawnArea = spawnArea;
        }

        public Bounds spawnArea(final Bounds bounds) {
            return spawnArea.apply(bounds);
        }
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
