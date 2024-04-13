package io.github.srcimon.screwbox.core.particles;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Environment;

import java.util.function.Supplier;

//TODO javadoc
//TODO package info
/**
 * Add particle effects to create some nice visuals.
 */
public interface Particles {

    /**
     * Returns the current count of particles in the {@link Environment#entities()}.
     */
    int particleCount();

    /**
     * Returns the count of particles spawned within the runtime of the engine.
     */
    long particlesSpawnCount();

    /**
     * Returns the current limit of particles. If the limit is reached no particle is spawned until an existing
     * particle times out.
     *
     * @see #setParticleLimit(int)
     */
    int particleLimit();

    /**
     * Sets the limit of particles. If the limit is reached no particle is spawned until an existing particle times out.
     * Can be used to enhance performance.
     *
     * @see #particleLimit()
     */
    Particles setParticleLimit(int limit);

    /**
     * Sets the max distance that particles will be spawened outside the fiel of vision.
     *
     * @see #spawnDistance()
     */
    Particles setSpawnDistance(double spawnDistance);

    /**
     * Returns the current distance that particles will be spawened outside the fiel of vision.
     *
     * @see #setSpawnDistance(double)
     */
    double spawnDistance();

    Particles spawn(Vector position, ParticleOptions options);

    default Particles spawn(final Vector position, final Supplier<ParticleOptions> options) {
        return spawn(position, options.get());
    }

    Particles spawnMultiple(int count, Vector position, ParticleOptions options);

    default Particles spawnMultiple(final int count, final Vector position, final Supplier<ParticleOptions> options) {
        return spawnMultiple(count, position, options.get());
    }

    Particles spawn(Bounds bounds, ParticleOptions options);

    default Particles spawn(final Bounds bounds, final Supplier<ParticleOptions> options) {
        return spawn(bounds, options.get());
    }

    Particles spawnMultiple(int count, Bounds bounds, ParticleOptions options);

    default Particles spawnMultiple(final int count, final Bounds bounds, final Supplier<ParticleOptions> options) {
        return spawnMultiple(count, bounds, options.get());
    }
}
