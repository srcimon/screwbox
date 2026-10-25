package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.physics.ChaoticMovementComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
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
        snake.root().remove(PhysicsComponent.class);
        snake.end().moveTo(snake.root().position().addX(120));
        screwBox.environment().addEntity(new Entity().add(new GravityComponent(Vector.y(400))));
        screwBox.environment().enableAllFeatures().addEntities(snake)
            .addSystem(new DebugGridSystem(16, 256));
        screwBox.start();
    }

    private static SoftPhysicsSupport.RopeEntities createSnake(Vector position, Engine screwBox) {
        var snake = SoftPhysicsSupport.createRope(position, position.addX(256), 9, screwBox.environment());
        snake.root().add(new RopeRenderComponent(Sprite.fromFile("snake.png")));
        snake.root().add(new CameraTargetComponent());
        snake.forEach(s -> s.add(new ChaoticMovementComponent(120, Duration.oneSecond())));
        snake.root().get(RopeComponent.class).isLengthFixed = true;
        snake.forEach(s -> s.get(PhysicsComponent.class).friction = 0.8);
        return snake;
    }
}