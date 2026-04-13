package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.rendering.MotionRotationComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.ShaderBundle;
import dev.screwbox.core.graphics.SplitScreenOptions;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.graphics.options.SpriteDrawOptions;
import dev.screwbox.core.graphics.postfilter.CrtMonitorPostFilter;
import dev.screwbox.core.graphics.postfilter.DeepSeaPostFilter;
import dev.screwbox.core.graphics.postfilter.FacetEyePostFilter;
import dev.screwbox.core.graphics.postfilter.HeatHazePostFilter;
import dev.screwbox.core.graphics.postfilter.UnderwaterPostFilter;
import dev.screwbox.core.graphics.postfilter.WarpPostFilter;
import dev.screwbox.playground.ai.BoidComponent;
import dev.screwbox.playground.ai.BoidContainerComponent;
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
            .addEntity(new Entity().name("container")
                .add(new BoidContainerComponent())
                .bounds(engine.graphics().visibleArea())
            )
            .addEntity(new Entity().name("cursor")
                .add(new CursorAttachmentComponent())
                .add(new BoidContainerComponent())
                .bounds(Bounds.atPosition(engine.mouse().position(), 120, 200))
            )
            .addEntity(new Entity().name("obstacle")
                .add(new BoidObstacleComponent())
                .bounds(Bounds.atOrigin(40, 40, 80, 80)))

            .addEntity(new Entity().name("obstacle")
                .add(new BoidObstacleComponent())
                .bounds(Bounds.atOrigin(-140, -40, 100, 40)))
        ;
        populateWithBoids(engine, 400);

        engine.start();
    }

    private static void populateWithBoids(Engine engine, int boidCount) {
        Sprite boidSprite = Sprite.pixel(Color.WHITE).scaled(4);
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
                });
            engine.environment().addEntity(boid1);
        }

    }

}