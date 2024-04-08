package io.github.srcimon.screwbox.core.environment.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.Rotation;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.SystemOrder;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.physics.ChaoticMovementComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroyComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacityComponent;
import io.github.srcimon.screwbox.core.graphics.Sprite;
import io.github.srcimon.screwbox.core.graphics.SpriteDrawOptions;

import java.util.Random;

@Order(SystemOrder.SIMULATION_BEGIN)
public class ParticleSystem implements EntitySystem {

    private static final Archetype PARTICLE_EMITTERS = Archetype.of(ParticleEmitterComponent.class, TransformComponent.class);

    private static final Random RANDOM = new Random();
    Sprite sprite = Sprite.fromFile("sprite-0001.png");

    @Override
    public void update(Engine engine) {
        for (var particleEmitter : engine.environment().fetchAll(PARTICLE_EMITTERS)) {
            final var emitter = particleEmitter.get(ParticleEmitterComponent.class);
            if (emitter.isActive && emitter.sheduler.isTick()) {
                var spawnPoint = emitter.useArea
                        ? particleEmitter.position().add(RANDOM.nextDouble(-0.5, 0.5) * particleEmitter.bounds().width(), RANDOM.nextDouble(-0.5, 0.5) * particleEmitter.bounds().height())
                        : particleEmitter.position();
                ChaoticMovementComponent chaoticMovementComponent = new ChaoticMovementComponent(60, Duration.ofMillis(1500), Vector.y(-100));
                engine.environment().addEntity(
                        new ParticleComponent(),
                        new PhysicsComponent(),
                        chaoticMovementComponent,
                        new TransformComponent(spawnPoint, 1, 1),
                        new RenderComponent(sprite, SpriteDrawOptions.scaled(5).rotation(Rotation.degrees(RANDOM.nextDouble() * 360))),
                        new TweenDestroyComponent(),
                        new TweenComponent(Duration.ofSeconds(4), TweenMode.SINE_IN_OUT),
                        new TweenOpacityComponent(Percent.zero(), Percent.of(0.2))
                );
            }
        }
    }

}
