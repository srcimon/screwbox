package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;

import java.util.Random;

import static java.util.Objects.nonNull;

public class ParticleEmitterSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class);
    private static final Random RANDOM = new Random();

    @Override
    public void update(final Engine engine) {
        for (final var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            if (nonNull(emitter.particle)) {
                engine.environment().addEntity(emitter.particle);
                emitter.particle = null;
            }
            if (emitter.isEnabled && emitter.sheduler.isTick(engine.loop().lastUpdate())) {
                initializeParticle(particleEmitter, emitter);
            }
        }
    }

    private void initializeParticle(final Entity particleEmitter, final ParticleEmitterComponent emitter) {
        final var spawnPoint = getSpawnPoint(particleEmitter, emitter);
        var physicsComponent = new PhysicsComponent();
        physicsComponent.ignoreCollisions = true;

        emitter.particle = new Entity()
                .add(physicsComponent)
                .add(new TweenComponent(Duration.ofSeconds(4), TweenMode.LINEAR_IN))
                .add(new TweenDestroyComponent())
                .add(new ParticleComponent(particleEmitter.id().orElse(null)))
                .add(new TransformComponent(spawnPoint, 1, 1));
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
