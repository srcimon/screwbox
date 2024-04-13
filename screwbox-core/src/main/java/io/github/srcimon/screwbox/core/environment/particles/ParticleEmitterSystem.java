package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;

import java.util.Objects;
import java.util.Random;

public class ParticleEmitterSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class);

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            if (emitter.isEnabled && emitter.sheduler.isTick(engine.loop().lastUpdate())) {
                final var render = particleEmitter.get(RenderComponent.class);
                final var particleOptions = Objects.nonNull(render)
                        ? emitter.particleOptions.drawOrderIfMissing(render.drawOrder)
                        : emitter.particleOptions;

                switch (emitter.spawnMode) {
                    case POSITION ->  engine.particles().spawn(particleEmitter.position(), particleOptions);
                    case AREA -> engine.particles().spawn(particleEmitter.bounds(), particleOptions);
                };
            }
        }
    }
}
