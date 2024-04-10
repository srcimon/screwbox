package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;

import java.util.Random;

@Order(SystemOrder.PARTICLES_CREATE)
public class ParticleCreateSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class);
    private static final Random RANDOM = new Random();

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            if (emitter.isEnabled && emitter.sheduler.isTick(engine.loop().lastUpdate())) {
                final var spawnPoint = getSpawnPoint(particleEmitter, emitter);
                emitter.particle = new Entity()
                        .add(new ParticleComponent(particleEmitter.id().orElse(null)))
                        .add(new TransformComponent(spawnPoint, 1, 1));
            }
        }
    }

    private Vector getSpawnPoint(final Entity particleEmitter, final ParticleEmitterComponent emitter) {
        return switch (emitter.spawnMode) {
            case POSITION -> particleEmitter.position();
            case AREA -> particleEmitter.position().add(
                    RANDOM.nextDouble(-0.5, 0.5) * particleEmitter.bounds().width(),
                    RANDOM.nextDouble(-0.5, 0.5) * particleEmitter.bounds().height());
        };
    }
}
