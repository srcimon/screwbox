package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.playground.ai.BoidComponent;
import dev.screwbox.playground.ai.BoidObstacleComponent;

import java.util.Random;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");
//engine.graphics().configuration().toggleFullscreen();
//engine.graphics().camera().setZoom(2);
//engine.graphics().configuration().setResolution(2560, 1440);
//          engine.graphics().postProcessing().addScreenFilter(new UnderwaterPostFilter()).addScreenFilter(new DeepSeaPostFilter());

        engine.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystemsFromPackage("dev.screwbox.playground")
//            .addEntity(new BoidSystemConfigComponent())
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
                .bounds(Bounds.atOrigin(40, 40, 80, 80)))

            .addEntity(new Entity().name("obstacle")
                .add(new BoidObstacleComponent())
                .bounds(Bounds.atOrigin(-140, -40, 100, 40)))
        ;
        populateWithBoids(engine, 80);

        engine.start();
    }

    private static void populateWithBoids(Engine engine, int boidCount) {
        Sprite boidSprite = SpriteBundle.DOT_WHITE.get();
        for (int i = 0; i < boidCount; i++) {
            Random random = new Random();
            Bounds area = engine.graphics().visibleArea();
            Entity boid1 = new Entity()
                .name("boid")
                .bounds(Bounds.atPosition(
                    random.nextDouble(area.minX(), area.maxX()),
                    random.nextDouble(area.minY(), area.maxY()),
                    16, 16))
                .add(new RenderComponent(boidSprite.replaceColor(Color.WHITE, Color.random())))
                .add(new PhysicsComponent())
                .add(new BoidComponent(), boid -> {
                    boid.velocity = 120;
                    boid.alignmentStrenth=8.2;
                    boid.separationStrength=9.3;
                    boid.cohesionStrength=10;
                    boid.perceptionRadius = 40;
                });
            engine.environment().addEntity(boid1);
        }
    }

}