package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;

import java.util.Objects;

@Order(SystemOrder.PARTICLES_SPAWN)
public class ParticleCreateSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);

            //TODO Move to particle spawn system
            if(Objects.nonNull(emitter.particleInPreparation)) {
                engine.environment().addEntity(emitter.particleInPreparation);
                emitter.particleInPreparation = null;
            }



            if (emitter.isEnabled && emitter.sheduler.isTick(engine.loop().lastUpdate())) {
                emitter.particleInPreparation = new Entity()
                        .add(new ParticleComponent(particleEmitter.id().orElse(null)));
            }
        }
    }
}
