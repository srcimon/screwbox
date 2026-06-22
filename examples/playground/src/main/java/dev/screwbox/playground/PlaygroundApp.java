package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.ai.BoidComponent;
import dev.screwbox.core.environment.ai.BoidObstacleComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.SpriteBundle;

import java.util.Random;

public class PlaygroundApp {

    static int i = 0;

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        screwBox.environment()
            .enableAllFeatures();

        for (int i = 0; i < 50; i++) {
            screwBox.environment()
                .addEntity(new Entity()
                    .add(new BoidComponent(), b -> {
                    })
                    .add(new PhysicsComponent())
                    .bounds(Bounds.atPosition(new Random().nextDouble(-100, 100), new Random().nextDouble(-100, 100), 8, 8))
                    .add(new RenderComponent(SpriteBundle.DOT_WHITE.get().scaled(0.5))));
        }
        screwBox.environment().addEntity(new Entity().bounds(Bounds.atPosition(0, 0, 400, 400)).add(new BoidObstacleComponent(), b -> b.isContainer = true));
        screwBox.start();
    }

}