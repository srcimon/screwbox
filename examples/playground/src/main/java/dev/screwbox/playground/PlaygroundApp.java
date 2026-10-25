package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.softphysics.RopeComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftPhysicsSupport;
import dev.screwbox.core.graphics.Sprite;

public class PlaygroundApp {


    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");
        screwBox.graphics().camera().setZoom(3);
        var snake = createSnake(Vector.$(0, 0), screwBox);
        snake.end().moveTo(snake.root().position().addX(120));
        screwBox.environment().enableAllFeatures().addEntities(snake)
            .addSystem(s -> {
                var target = s.environment().fetchSingleton(Archetype.of(CameraTargetComponent.class));
                target.get(PhysicsComponent.class).velocity = s.keyboard().wsadMovement(60);
            })
            .addSystem(new DebugGridSystem(16, 256));
        screwBox.start();
    }

    private static SoftPhysicsSupport.RopeEntities createSnake(Vector position, Engine screwBox) {
        var snake = SoftPhysicsSupport.createRope(position, position.addX(256), 17, screwBox.environment());
        snake.root().add(new RopeRenderComponent(Sprite.fromFile("snake.png")));
        snake.root().get(RopeComponent.class).isLengthFixed = true;
        snake.root().add(new CameraTargetComponent(10));
        snake.forEach(s -> s.get(PhysicsComponent.class).friction = 0.8);
        return snake;
    }
}