package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.MotionRotationComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.playground.ai.BoidComponent;

import java.util.Random;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystemsFromPackage("dev.screwbox.playground");

        populateWithBoids(engine, 100);

        engine.start();
    }

    private static void populateWithBoids(Engine engine, int boidCount) {
        Sprite boidSprite = Sprite.fromFile("boid.png");
        for (int i = 0; i < boidCount; i++) {
            Random random = new Random();
            Bounds area = engine.graphics().visibleArea();
            engine.environment().addEntity(new Entity()
                .name("boid")
                .bounds(Bounds.atPosition(
                    random.nextDouble(area.minX(), area.maxX()),
                    random.nextDouble(area.minY(), area.maxY()),
                    16, 160))
                .add(new RenderComponent(boidSprite))
                .add(new MotionRotationComponent())
                .add(new PhysicsComponent(Vector.random(60)))
                .add(new BoidComponent()));
        }
    }

}