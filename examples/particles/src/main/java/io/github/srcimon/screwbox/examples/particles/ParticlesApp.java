package io.github.srcimon.screwbox.examples.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.SpritesBundle;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.debug.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleDebugSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleDesigner;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.environment.tweening.TweenMode;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.Duration.ofSeconds;
import static io.github.srcimon.screwbox.core.utils.Sheduler.withInterval;

public class ParticlesApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Particles");
        screwBox.loop().unlockFps();
        screwBox.environment()
                .addSystem(engine -> engine.window().setTitle("E" + engine.environment().entityCount()))
//                .addSystem(e -> e.environment().createSavegame("test-serialization.sav"))
                .addEntity("particle emitter",
                        new TransformComponent(Vector.zero(), 128, 128),
                        new ParticleEmitterComponent(withInterval(ofMillis(50)), ParticleDesigner
                                .useTemplate(SpritesBundle.SMOKE_16)
                                .tweenMode(TweenMode.SINE_IN_OUT)
                                .randomStartScale(6, 8)
                                .animateOpacity(Percent.zero(), Percent.of(0.1))
                                .baseMovement(Vector.$(40, -100))
                                .chaoticMovement(50, ofSeconds(1))
                                .drawOrder(2)
                                .randomStartRotation()
                                .lifetimeSeconds(2)))
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