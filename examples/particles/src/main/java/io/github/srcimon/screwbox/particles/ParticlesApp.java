package io.github.srcimon.screwbox.particles;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.environment.core.LogFpsSystem;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent;
import io.github.srcimon.screwbox.core.environment.particles.ParticleInteractionComponent;
import io.github.srcimon.screwbox.core.environment.physics.CursorAttachmentComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

import static io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterComponent.SpawnMode.AREA;
import static io.github.srcimon.screwbox.core.particles.ParticleOptionsBundle.CONFETTI;

public class ParticlesApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Particles");

        screwBox.environment()
                .enableTweening()
                .enableParticles()
                .enablePhysics()
                .addEntity("particle spawner",
                        new TransformComponent(screwBox.graphics().world().visibleArea()),
                        new ParticleEmitterComponent(Duration.ofMillis(10), AREA, CONFETTI))
                .addSystem(new LogFpsSystem())
                .addEntity("particle interactor",
                        new CursorAttachmentComponent(),
                        new PhysicsComponent(),
                        new TransformComponent(-60, -60, 120, 120),
                        new ParticleInteractionComponent(80))
                .enableRendering();

        screwBox.start();
    }
}