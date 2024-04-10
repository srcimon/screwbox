package io.github.srcimon.screwbox.examples.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.debug.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleDebugSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleRenderCustomizeComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleTimeToLiveConfigComponent;

import static io.github.srcimon.screwbox.core.Duration.ofMillis;
import static io.github.srcimon.screwbox.core.utils.Sheduler.withInterval;

public class ParticlesApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Particles");

        screwBox.environment()
                .addEntity("particle emitter",
                        new TransformComponent(Vector.zero(), 128, 128),
                        new ParticleRenderCustomizeComponent(),
                        new ParticleTimeToLiveConfigComponent(Duration.ofSeconds(5), Percent.half()),
                        new ParticleEmitterComponent(withInterval(ofMillis(20))))
                .addSystem(new ParticleDebugSystem())
                .addSystem(new LogFpsSystem())
                .enableRendering()
                .enableTweening()
                .enableParticles();

        screwBox.start();
    }
}