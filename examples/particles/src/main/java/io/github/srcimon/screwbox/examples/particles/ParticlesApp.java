package io.github.srcimon.screwbox.examples.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.assets.ParticleDesignerBundle;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.debug.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleDebugSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.utils.Sheduler.withInterval;

public class ParticlesApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Particles");
//       screwBox.loop().unlockFps();
        screwBox.environment()
//                .addSystem(e -> e.environment().createSavegame("test-serialization.sav"))
                .addEntity("particle emitter",
                        new TransformComponent(Vector.zero().addX(-200), 128, 128),
                        new ParticleEmitterComponent(withInterval(ofMillis(50)), ParticleDesignerBundle.SMOKE.get()
                                .startScale(10)))
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