package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacityComponent;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

import java.util.Random;

import static io.github.srcimon.screwbox.core.assets.SpritesBundle.BLOB_ANIMATED_16;

public class ParticleSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class, TransformComponent.class);

    private static final Random RANDOM = new Random();

    @Override
    public void update(Engine engine) {
        for (var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            if (emitter.sheduler.isTick()) {
            var spawnPoint = emitter.useArea
                    ? particleEmitter.position()
//                        ? particleEmitter.position().add(RANDOM.nextDouble(-0.5, 0.5) * particleEmitter.bounds().width(), RANDOM.nextDouble(-0.5, 0.5) * particleEmitter.bounds().height())
                    : particleEmitter.position();
                engine.environment().addEntity(
                        new TransformComponent(spawnPoint, 1, 1),
                        new RenderComponent(BLOB_ANIMATED_16, SpriteDrawOptions.scaled(2)),
                        new TweenDestroyComponent(),
                        new TweenComponent(Duration.ofSeconds(2)),
                        new TweenOpacityComponent());
        }
        }
    }

}
