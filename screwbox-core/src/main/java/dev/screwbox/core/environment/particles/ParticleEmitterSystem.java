package dev.screwbox.core.environment.particles;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.core.TransformComponent;

public class ParticleEmitterSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(TransformComponent.class, ParticleEmitterComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            if (emitter.isEnabled && emitter.scheduler.isTick(engine.loop().time())) {
                final var particleOptions = emitter.particleOptions.source(particleEmitter);
                final var spawnArea = emitter.spawnMode.spawnArea(particleEmitter.bounds());
                engine.particles().spawn(spawnArea, particleOptions);
            }
        }
    }
}
