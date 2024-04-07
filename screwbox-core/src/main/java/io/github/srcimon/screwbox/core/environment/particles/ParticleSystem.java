package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.SpritesBundle;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.ChaoticMovementComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RotateSpriteComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacityComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteBatch;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

import java.util.Random;

import static io.github.srcimon.screwbox.core.assets.SpritesBundle.BLOB_ANIMATED_16;

@Order(SystemOrder.SIMULATION_BEGIN)
public class ParticleSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class, TransformComponent.class);

    private static final Random RANDOM = new Random();
    Sprite sprite = Sprite.fromFile("spark.png");

    @Override
    public void update(Engine engine) {
        for (var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            if (emitter.sheduler.isTick()) {
            var spawnPoint = emitter.useArea
                        ? particleEmitter.position().add(RANDOM.nextDouble(-0.5, 0.5) * particleEmitter.bounds().width(), RANDOM.nextDouble(-0.5, 0.5) * particleEmitter.bounds().height())
                    : particleEmitter.position();
            double scale = 2;

            //SMOKE
//                engine.environment().addEntity(
//                        new PhysicsComponent(Vector.y(-200 + RANDOM.nextDouble(-20, 20)).addX(RANDOM.nextDouble(-30, 30))),
//                        new TransformComponent(spawnPoint, 1, 1),
//                        new RenderComponent(sprite, SpriteDrawOptions.scaled(scale).rotation(Rotation.degrees(RANDOM.nextDouble() * 360))),
//                        new TweenDestroyComponent(),
//                        new TweenComponent(Duration.ofSeconds(1), TweenMode.SINE_IN_OUT),
//                        new TweenOpacityComponent(Percent.zero(), Percent.of(0.2))
//                );
                engine.environment().addEntity(
                        new PhysicsComponent(),
                        new ChaoticMovementComponent(10, Duration.ofMillis(1500)),
                        new TransformComponent(spawnPoint, 1, 1),
                        new RenderComponent(sprite, SpriteDrawOptions.scaled(scale).rotation(Rotation.degrees(RANDOM.nextDouble() * 360))),
                        new TweenDestroyComponent(),
                        new TweenComponent(Duration.ofSeconds(20), TweenMode.SINE_IN_OUT),
                        new TweenOpacityComponent(Percent.zero(), Percent.of(0.9))
                );
        }
        }
    }

}
