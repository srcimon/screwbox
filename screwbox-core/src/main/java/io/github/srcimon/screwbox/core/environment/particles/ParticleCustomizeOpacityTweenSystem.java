package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.physics.ChaoticMovementComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacityComponent;

import static java.util.Objects.nonNull;

public class ParticleCustomizeOpacityTweenSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class, ParticleCustomizeOpacityTweenComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            final var opacityConfig = particleEmitter.get(ParticleCustomizeOpacityTweenComponent.class);
            if (nonNull(emitter.particle)) {
                emitter.particle.add(new TweenOpacityComponent(opacityConfig.from, opacityConfig.to));
            }
        }
    }
}
