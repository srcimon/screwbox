package io.github.srcimon.screwbox.core.particles;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.particles.ParticleOptions;

import java.util.function.Supplier;

//TODO javadoc
//TODO package info
public interface Particles {

    int particleCount();

    long particlesSpawnCount();

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

//     engine.particles()
//             .particleCount()
//                        .particlesSpawnedCount()
//                        .setRenderDistance(600)
//                        .setLimit(1000)
//                        .spawn(spawnPoint, designer) // ParticleDesigner -> ParticleConfiguration (creation is done by particles.spawn())!!!
//                        .spawn(renderArea, designer)
//                        .spawnMultiple(20, area, designer);
}
