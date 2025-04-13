package dev.screwbox.core.environment.particles;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.core.particles.Particles;
import dev.screwbox.core.utils.Scheduler;

import java.io.Serial;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ParticleEmitterComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Specify the area where {@link Particles} are emitted.
     */
    public enum SpawnMode {
        /**
         * Spawn {@link Particles} right from the center of the {@link Bounds}.
         */
        POSITION(bounds -> Bounds.atPosition(bounds.position(), 0, 0)),

        /**
         * Spawn {@link Particles} from the whole {@link Bounds}.
         */
        AREA(bounds -> bounds),

        /**
         * Spawn {@link Particles} only from the left side of the {@link Bounds}.
         */
        LEFT_SIDE(bounds -> Bounds.atOrigin(bounds.origin(), 0, bounds.height())),

        /**
         * Spawn {@link Particles} only from the right side of the {@link Bounds}.
         */
        RIGHT_SIDE(bounds -> Bounds.atOrigin(bounds.origin().addX(bounds.width()), 0, bounds.height())),

        /**
         * Spawn {@link Particles} only from the top side of the {@link Bounds}.
         */
        TOP_SIDE(bounds -> Bounds.atOrigin(bounds.origin(), bounds.width(), 0)),

        /**
         * Spawn {@link Particles} only from the bottom side of the {@link Bounds}.
         */
        BOTTOM_SIDE(bounds -> Bounds.atOrigin(bounds.origin().addY(bounds.height()), bounds.width(),0));

        private final UnaryOperator<Bounds> spawnArea;

        SpawnMode(final UnaryOperator<Bounds> spawnArea) {
            this.spawnArea = spawnArea;
        }

        public Bounds spawnArea(final Bounds bounds) {
            return spawnArea.apply(bounds);
        }
    }

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
