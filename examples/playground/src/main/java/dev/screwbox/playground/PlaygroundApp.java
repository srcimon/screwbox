package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.ai.BoidComponent;
import dev.screwbox.core.environment.ai.BoidObstacleComponent;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.ChaoticMovementComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;

import java.util.Random;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystemsFromPackage("dev.screwbox.playground")
            .addEntity(new Entity().name("container")
                .add(new BoidObstacleComponent(), config -> config.isContainer = true)
                .bounds(engine.graphics().visibleArea())
            )
            .addEntity(new Entity().name("cursor")
                .add(new CursorAttachmentComponent())
                .add(new BoidObstacleComponent(), config -> config.isContainer = true)
                .bounds(Bounds.atPosition(engine.mouse().position(), 120, 200))
            )
            .addEntity(new Entity().name("obstacle")
                .add(new BoidObstacleComponent())
                .add(new PhysicsComponent())
                .add(new ChaoticMovementComponent(200, Duration.ofSeconds(2)))
                .bounds(Bounds.atOrigin(40, 40, 80, 80)))

            .addEntity(new Entity().name("obstacle")
                .add(new BoidObstacleComponent())
                .add(new PhysicsComponent())
                .add(new ChaoticMovementComponent(200, Duration.ofSeconds(2)))
                .bounds(Bounds.atOrigin(-140, -40, 100, 40)))
        ;
        populateWithBoids(engine, 3000);

        engine.start();
    }

    private static void populateWithBoids(Engine engine, int boidCount) {
        Random random = new Random();
        Sprite boidSprite = Sprite.pixel(Color.WHITE).scaled(1);
        for (int i = 0; i < boidCount; i++) {

            Bounds area = engine.graphics().visibleArea();
            Entity boid1 = new Entity()
                .name("boid")
                .bounds(Bounds.atPosition(
                    random.nextDouble(area.minX(), area.maxX()),
                    random.nextDouble(area.minY(), area.maxY()),
                    16, 16))
                .add(new RenderComponent(boidSprite.replaceColor(Color.WHITE, Color.RED)))
                .add(new PhysicsComponent())
                .add(new BoidComponent(), boid -> {
                    boid.velocity = 120;
                    boid.alignmentStrenth = 8.2;
                    boid.separationStrength = 10.3;
                    boid.cohesionStrength = 5;

                    boid.obstacleAvoidanceStrength = 48;
                    boid.obstaclePerceptionRadius=100;
                    boid.perceptionRadius = 100;
                });
            engine.environment().addEntity(boid1);
        }
    }

}