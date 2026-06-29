package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.particles.ParticleEmitterComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.TailwindComponent;
import dev.screwbox.core.particles.ParticlesBundle;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Hello world");

        screwBox.environment()
            .enableAllFeatures()
            .addEntity(new Entity()
                .add(new TransformComponent())
                .add(new CursorAttachmentComponent())
                .add(new ParticleEmitterComponent(Duration.ofMillis(100), ParticlesBundle.CONFETTI))
                .add(new TailwindComponent(40, Percent.max())));

        screwBox.start();
    }
}