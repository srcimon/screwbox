package io.github.srcimon.screwbox.core.particles;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Environment;

import java.util.function.Supplier;

//TODO javadoc
//TODO package info
public interface Particles {

    /**
     * Returns the current count of particles in the {@link Environment#entities()}.
     */
    int particleCount();

    long particlesSpawnCount();

    int limit();

    Particles setRenderDistance(double renderDistance);

    double renderDistance();

    Particles setLimit(int limit);

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
