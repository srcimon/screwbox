package io.github.srcimon.screwbox.core.particles;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;

import java.util.function.Supplier;

/**
 * Add particle effects to create some nice visuals. Can be automated by using {@link ParticleEmitterComponent}.
 */
public interface Particles {

    /**
     * Returns the area within particles are spawned.
     */
    Bounds spawnArea();

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

    /**
     * Spawns a new particle into the {@link Environment} using the given position.
     */
    Particles spawn(Vector position, ParticleOptions options);

    /**
     * Spawns a new particle into the {@link Environment} using the given position.
     */
    default Particles spawn(final Vector position, final Supplier<ParticleOptions> options) {
        return spawn(position, options.get());
    }

    /**
     * Spawns multiple new particles into the {@link Environment} using the given position.
     */
    Particles spawnMultiple(int count, Vector position, ParticleOptions options);

    /**
     * Spawns multiple new particles into the {@link Environment} using the given position.
     */
    default Particles spawnMultiple(final int count, final Vector position, final Supplier<ParticleOptions> options) {
        return spawnMultiple(count, position, options.get());
    }

    /**
     * Spawns a new particles into the {@link Environment} using a random position within the given {@link Bounds}.
     */
    Particles spawn(Bounds bounds, ParticleOptions options);

    /**
     * Spawns a new particles into the {@link Environment} using a random position within the given {@link Bounds}.
     */
    default Particles spawn(final Bounds bounds, final Supplier<ParticleOptions> options) {
        return spawn(bounds, options.get());
    }

    /**
     * Spawns multiple new particles into the {@link Environment} using a random position within the given {@link Bounds}.
     */
    Particles spawnMultiple(int count, Bounds bounds, ParticleOptions options);

    /**
     * Spawns multiple new particles into the {@link Environment} using a random position within the given {@link Bounds}.
     */
    default Particles spawnMultiple(final int count, final Bounds bounds, final Supplier<ParticleOptions> options) {
        return spawnMultiple(count, bounds, options.get());
    }
}
