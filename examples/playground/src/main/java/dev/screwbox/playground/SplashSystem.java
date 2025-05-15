package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Ease;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.audio.SoundOptions;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.FloatComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.core.particles.ParticleOptions;
import dev.screwbox.core.particles.SpawnMode;
import dev.screwbox.core.utils.ListUtil;

import java.util.Random;

public class SplashSystem implements EntitySystem {

    private static final Random RANDOM = new Random();

    @Override
    public void update(Engine engine) {
        if(engine.keyboard().isDown(Key.Q)) {
            return;
        }
        for (final var entity : engine.environment().fetchAll(Archetype.of(SplashComponent.class, FloatComponent.class, PhysicsComponent.class))) {
            var floatComponent = entity.get(FloatComponent.class);
            if (floatComponent.attachedWave != null && entity.get(PhysicsComponent.class).momentum.length() > 15) {
                SplashComponent splashComponent = entity.get(SplashComponent.class);
                if(splashComponent.soundScheduler.isTick()) {
                    engine.audio().playSound(ListUtil.randomFrom(splashComponent.sounds), SoundOptions.playOnce()
                            .speed(RANDOM.nextDouble(0.1,2.2))//TODO configure ranges in splashcomponent
                            .position(entity.position())
                    );
                }
                if (splashComponent.scheduler.isTick()) {
                    engine.particles().spawn(Bounds.atOrigin(entity.bounds().minX(), floatComponent.attachedWave.middle().y(), entity.bounds().width(), 2)
                            , SpawnMode.BOTTOM_SIDE, ParticleOptions.particleSource(entity)
                                    .chaoticMovement(60, Duration.ofSeconds(1))
                                    .animateOpacity(Percent.of(0.1), Percent.quarter())
                                    .sprite(Sprite.fromFile("splash.png"))
                                    .randomRotation(-0.2, 0.2)
                                    .randomBaseSpeed(10)
                                    .ease(Ease.SINE_IN_OUT)
                                    .randomRotation(0.25)
                                    .randomLifeTimeMilliseconds(400, 800)
                                    .animateScale(0.5, 0.6));
                }
            }
        }
    }
}
