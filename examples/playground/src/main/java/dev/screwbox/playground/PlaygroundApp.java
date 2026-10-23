package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.Sprite;
import dev.screwbox.core.utils.Scheduler;

public class PlaygroundApp {

    static Scheduler scheduler = Scheduler.everySecond();

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");
        screwBox.graphics().camera().setZoom(2);
        Sprite sprite = Sprite.placeholder(Color.RED, Size.square(16));
        for (int x = 0; x < 10000; x += 16) {
            for (int y = 0; y < 10000; y += 16) {
                screwBox.environment().addEntity(new Entity()
                    .bounds(Bounds.atOrigin(Vector.of(x, y), 16, 16))
                    .add(new ColliderComponent())
                    .add(new ChunkComponent())
                    .add(new RenderComponent(sprite)));
            }
        }
        screwBox.loop().unlockFps();
        screwBox.environment()
            .enableAllFeatures()
            .addSystem(new ChunkSystem())
            .addEntity(new Entity().add(new PhysicsComponent()).bounds(Bounds.atOrigin(Vector.of(0, 0), 16, 16)))
            .addEntity(new Entity().add(new PhysicsComponent()).bounds(Bounds.atOrigin(Vector.of(0, 0), 16, 16)))
            .addEntity(new Entity().add(new PhysicsComponent()).bounds(Bounds.atOrigin(Vector.of(0, 0), 16, 16)))
            .addEntity(new Entity().add(new PhysicsComponent()).bounds(Bounds.atOrigin(Vector.of(0, 0), 16, 16)))
            .addSystem(new LogFpsSystem())
            .addSystem(Order.PREPARATION, s -> {
                if(scheduler.isTick()) {
                    System.out.println(screwBox.environment().entityCount());
                }
                s.graphics().camera().move(s.keyboard().wsadMovement(s.loop().delta() * 500));
            });

        screwBox.start();
    }
}