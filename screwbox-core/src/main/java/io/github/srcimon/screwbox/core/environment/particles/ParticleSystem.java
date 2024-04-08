package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

import java.util.Random;

@Order(SystemOrder.SIMULATION_BEGIN)
public class ParticleSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class, TransformComponent.class);

    private static final Random RANDOM = new Random();


    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            if (emitter.isActive && emitter.sheduler.isTick()) {
                var spawnPoint = emitter.useAreaSpawn
                        ? particleEmitter.position().add(RANDOM.nextDouble(-0.5, 0.5) * particleEmitter.bounds().width(), RANDOM.nextDouble(-0.5, 0.5) * particleEmitter.bounds().height())
                        : particleEmitter.position();

                var particle = emitter.particleFactory.createParticle(spawnPoint);
                engine.environment().addEntity(particle);
            }
        }
    }

}
