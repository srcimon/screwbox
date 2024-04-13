package io.github.srcimon.screwbox.core.particles;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.particles.ParticleOptions;

//TODO javadoc
//TODO package info
public interface Particles {

    int particleCount();

    Particles spawn(Vector position, ParticleOptions options);

    Particles spawnMultiple(int count, Vector position, ParticleOptions options);

    Particles spawn(Bounds bounds, ParticleOptions options);

    Particles spawnMutliple(int count, Bounds bounds, ParticleOptions options);

//     engine.particles()
//             .particleCount()
//                        .particlesSpawnedCount()
//                        .setRenderDistance(600)
//                        .setLimit(1000)
//                        .spawn(spawnPoint, designer) // ParticleDesigner -> ParticleConfiguration (creation is done by particles.spawn())!!!
//                        .spawn(renderArea, designer)
//                        .spawnMultiple(20, area, designer);
}
