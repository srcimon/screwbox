package io.github.srcimon.screwbox.examples.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleDebugSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;

public class ParticlesApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Particles");

        engine.environment()
                .addEntity("particle emitter",
                        new TransformComponent(Vector.zero(), 128, 128),
                        new ParticleEmitterComponent())
                .addSystem(new ParticleDebugSystem())
                .enableParticles();

        engine.start();
    }
}