package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;

import java.util.Random;

import static java.util.Objects.nonNull;

public class ParticleCustomizeTweenSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class, ParticleCustomizeTweenComponent.class);
    private static final Random RANDOM = new Random();

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            final var tweenConfig = particleEmitter.get(ParticleCustomizeTweenComponent.class);
            if (nonNull(emitter.particle)) {
                long minNanos =  (long)(tweenConfig.timeToLive.nanos() * (1- tweenConfig.abrevation.value()));
                long maxNanos =  (long)(tweenConfig.timeToLive.nanos() * (1+ tweenConfig.abrevation.value()));
                var duration = Duration.ofNanos(RANDOM.nextLong(minNanos, maxNanos));
                var tween = emitter.particle.get(TweenComponent.class);
                tween.mode = tweenConfig.mode;
                tween.duration = duration;
            }
        }
    }
}
