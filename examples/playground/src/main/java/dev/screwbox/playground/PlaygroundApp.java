package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.MotionRotationComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.postfilter.DeepSeaPostFilter;
import dev.screwbox.core.graphics.postfilter.UnderwaterPostFilter;
import dev.screwbox.playground.ai.BoidComponent;
import dev.screwbox.playground.ai.BoidObstacleComponent;

import java.util.Random;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

//          engine.graphics().postProcessing().addScreenFilter(new UnderwaterPostFilter()).addScreenFilter(new DeepSeaPostFilter());
        engine.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystemsFromPackage("dev.screwbox.playground")
            .addEntity(new Entity().name("cursor")
                .add(new CursorAttachmentComponent())
                .add(new BoidObstacleComponent())
                .bounds(Bounds.atPosition(engine.mouse().position(), 120, 200))
            )
            .addEntity(new Entity().name("obstacle")
                .add(new BoidObstacleComponent())
                .bounds(Bounds.atOrigin(40, 40, 80, 80)))

            .addEntity(new Entity().name("obstacle")
                .add(new BoidObstacleComponent())
                .bounds(Bounds.atOrigin(-140, -40, 100, 40)))
        ;
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
                    16, 16))
                .add(new RenderComponent(boidSprite.replaceColor(Color.WHITE, Color.random()), SpriteDrawOptions.scaled(random.nextDouble(0.5,1.25))))
                .add(new MotionRotationComponent())
                .add(new PhysicsComponent())
                .add(new BoidComponent(), boid -> {
                    boid.velocity = random.nextDouble(50,150);
                }));
        }
    }

}