package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.utils.ListUtil;

import static java.util.Objects.nonNull;






// TODO EIGENES SYSTEM ZUM BEREINIGEN ALTER PARTICLES, TWEEN DESTROY NICHT VERWENDEN!!!!!








//TODO RENAME!!!!
@Order(SystemOrder.PARTICLES_CUSTOMIZE)
public class ParticleTimeToLiveConfigSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class, ParticleTimeToLiveConfigComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            final var timeToLiveConfig = particleEmitter.get(ParticleTimeToLiveConfigComponent.class);
            if (nonNull(emitter.particle)) {
                long nanosToLive = timeToLiveConfig.timeToLive.nanos(); //TODO CALC ABREVATION  //timeToLiveConfig.abrevation.value();
                emitter.particle.add(new TweenComponent(Duration.ofNanos(nanosToLive), timeToLiveConfig.mode))
                        .add(new TweenDestroyComponent());
            }
        }
    }
}
