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
    private static final Random RANDOM = new Random();

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {

            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            if (emitter.isEnabled && emitter.sheduler.isTick(engine.loop().lastUpdate())) {
                final var render = particleEmitter.get(RenderComponent.class);
                final var spawnPoint = getSpawnPoint(particleEmitter, emitter);
                final var actualOptions = Objects.nonNull(render)
                        ? emitter.particleOptions.drawOrderIfMissing(render.drawOrder)
                        : emitter.particleOptions;
                engine.particles().spawn(spawnPoint, actualOptions);
            }
        }
    }

    //TODO move into particles
    private Vector getSpawnPoint(final Entity particleEmitter, final ParticleEmitterComponent emitter) {
        return switch (emitter.spawnMode) {
            case POSITION -> particleEmitter.position();
            case AREA -> particleEmitter.position().add(
                    RANDOM.nextDouble(-0.5, 0.5) * particleEmitter.bounds().width(),
                    RANDOM.nextDouble(-0.5, 0.5) * particleEmitter.bounds().height());
        };
    }
}
