package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.ChaoticMovementComponent;

import static java.util.Objects.nonNull;

public class ParticleCustomizeChaoticMovementSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class, ParticleCustomizeChaoticMovementComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            final var movementConfgi = particleEmitter.get(ParticleCustomizeChaoticMovementComponent.class);
            if (nonNull(emitter.particle)) {
                emitter.particle.add(new ChaoticMovementComponent(movementConfgi.speed, movementConfgi.interval, movementConfgi.baseSpeed));
            }
        }
    }
}
