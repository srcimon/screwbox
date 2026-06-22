package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.ai.BoidComponent;
import dev.screwbox.core.environment.ai.BoidObstacleComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.SpriteBundle;

import java.util.Random;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");

        screwBox.environment()
            .enableAllFeatures();

        var offset= Vector.$(2000,2000);

        for (int i = 0; i < 4000; i++) {
            screwBox.environment()
                .addEntity(new Entity()
                    .add(new BoidComponent(), b -> {
                        b.velocity = 100;
                        b.perceptionRadius = 25;
                        b.alignmentStrenth = 25;
                        b.separationStrength = 28;
                        b.cohesionStrength = 3;
                    })
                    .add(new PhysicsComponent())
                    .bounds(Bounds.atPosition(new Random().nextDouble(-400, 400), new Random().nextDouble(-400, 400), 2, 2).moveBy(offset))
                    .add(new RenderComponent(SpriteBundle.DOT_WHITE.get().scaled(0.25))));
        }
        screwBox.environment().addEntity(new Entity().add(new CameraTargetComponent()).bounds(Bounds.atPosition(offset, 800, 800)).add(new BoidObstacleComponent(), b -> b.isContainer = true));
        screwBox.start();
    }

}