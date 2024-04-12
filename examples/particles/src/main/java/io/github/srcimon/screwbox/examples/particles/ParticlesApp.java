package io.github.srcimon.screwbox.examples.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.ParticleDesignerBundle;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.debug.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleDebugSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleDesigner;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.utils.Sheduler.withInterval;

public class ParticlesApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Particles");
//       screwBox.loop().unlockFps();
        screwBox.environment()
//                .addSystem(e -> e.environment().createSavegame("test-serialization.sav"))
                .addSystem(engine -> {
                    if(engine.mouse().isPressedLeft()) {
                        engine.physics().searchAtPosition(engine.mouse().position())
                                .checkingFor(Archetype.of(TransformComponent.class, ParticleEmitterComponent.class))
                                .selectAny().ifPresent(e -> {
                                    e.get(ParticleEmitterComponent.class).isEnabled = !e.get(ParticleEmitterComponent.class).isEnabled;
                                });
                    }
                })
                .addEntity("particle emitter",
                        new TransformComponent(Vector.zero().addX(-200), 128, 128),
                        new ParticleEmitterComponent(withInterval(ofMillis(50)), new ParticleDesigner()
                                .tweenMode(TweenMode.LINEAR_OUT)
                                .animateScale(5, 2)
                                .randomLifeTimeSeconds(1, 5)
                                .animateOpacity()))
                .addEntity("particle emitter",
                        new TransformComponent(Vector.zero(), 128, 128),
                        new ParticleEmitterComponent(withInterval(ofMillis(50)), ParticleDesignerBundle.SMOKE))
                .addEntity("particle emitter",
                        new TransformComponent(Vector.zero().addX(200), 128, 128),
                        new ParticleEmitterComponent(withInterval(ofMillis(50)), ParticleDesignerBundle.SMOKE.get()
                                .startScale(2)
                                .chaoticMovement(5, Duration.ofMillis(20))))
                .addSystem(new ParticleDebugSystem())
                .addSystem(new LogFpsSystem())
                .enableRendering()
                .enablePhysics()
                .enableTweening()
                .enableParticles()
        ;

        screwBox.start();
    }
}