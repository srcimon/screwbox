package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;

public class ParticleEmitterSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            if (emitter.isEnabled && emitter.sheduler.isTick(engine.loop().lastUpdate())) {
                final var particleOptions = emitter.particleOptions.source(particleEmitter);
                final var spawnArea = emitter.spawnMode.spawnArea(particleEmitter.bounds());
                engine.particles().spawn(spawnArea, particleOptions);

                switch (emitter.spawnMode) {
                    case POSITION -> engine.particles().spawn(particleEmitter.position(), particleOptions);
                    case AREA -> engine.particles().spawn(particleEmitter.bounds(), particleOptions);
                    case LEFT -> engine.particles().spawn(Bounds.atOrigin(particleEmitter.bounds().origin(), 1, particleEmitter.bounds().height()), particleOptions);
                }

            }
        }
    }
}
