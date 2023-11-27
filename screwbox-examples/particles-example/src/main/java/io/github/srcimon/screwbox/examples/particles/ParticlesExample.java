package io.github.srcimon.screwbox.examples.particles;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.entities.Entity;
import io.github.srcimon.screwbox.core.entities.components.TransformComponent;
import io.github.srcimon.screwbox.core.entities.systems.LogFpsSystem;
import io.github.srcimon.screwbox.core.entities.systems.QuitOnKeyPressSystem;

import static io.github.srcimon.screwbox.core.Bounds.atPosition;

public class ParticlesExample {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Particles Example");

        screwBox.entities()
                .add(new Entity()
                        .add(new TransformComponent(atPosition(0, 0, 64, 64)))

                )
                .add(new LogFpsSystem())
                .add(new QuitOnKeyPressSystem())
                .add(new DrawEntityOutlineSystem());

        screwBox.start();
    }
}
