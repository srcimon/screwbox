package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent.SpawnMode;

public class ParticleEmitterSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            if (emitter.isEnabled && emitter.sheduler.isTick(engine.loop().lastUpdate())) {
                final var particleOptions = emitter.particleOptions.source(particleEmitter);
                if (SpawnMode.AREA.equals(emitter.spawnMode)) {
                    engine.particles().spawn(particleEmitter.bounds(), particleOptions);
                } else {
                    engine.particles().spawn(particleEmitter.position(), particleOptions);
                }
            }
        }
    }
}
