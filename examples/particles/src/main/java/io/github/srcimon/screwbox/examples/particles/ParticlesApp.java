package io.github.srcimon.screwbox.examples.particles;

import io.github.srcimon.screwbox.core.Duration;
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
import static io.github.srcimon.screwbox.core.utils.Sheduler.withInterval;

public class ParticlesApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Particles");

        screwBox.environment()
                .addEntity("particle emitter",
                        new TransformComponent(Vector.zero(), 128, 128),
                        new ParticleEmitterComponent(withInterval(ofMillis(10)), ParticleDesigner
                                .useTemplate(SpritesBundle.SMOKE_16)
                                .tweenMode(TweenMode.SINE_IN_OUT)
                                .startScale(4)
                               /* .animateOpacity(Percent.zero(), Percent.of(0.1))


                                .drawOrder(2)
                                .randomStartRotation())
                                .baseMovement(Vector.y(-100))
                                .chaoticMovement(50, Duration.ofSeconds(1))
                                .lifetimeSeconds(2)
                                .randomLifetimeMillis(500)*/))
                .addSystem(new ParticleDebugSystem())
                .addSystem(new LogFpsSystem())
                .enableRendering()
                .enablePhysics()
                .enableTweening()
                .enableParticles();

        screwBox.start();
    }
}