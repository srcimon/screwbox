package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;

import static java.util.Objects.nonNull;

public class ParticleCustomizeTweenSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class, ParticleCustomizeTweenComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            final var timeToLiveConfig = particleEmitter.get(ParticleCustomizeTweenComponent.class);
            if (nonNull(emitter.particle)) {
                long nanosToLive = timeToLiveConfig.timeToLive.nanos(); //TODO CALC ABREVATION  //timeToLiveConfig.abrevation.value();
                emitter.particle.add(new TweenComponent(Duration.ofNanos(nanosToLive), timeToLiveConfig.mode))
                        .add(new TweenDestroyComponent());
            }
        }
    }
}
