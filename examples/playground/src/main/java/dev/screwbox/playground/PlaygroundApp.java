package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.keyboard.Key;

import java.util.List;

public class PlaygroundApp {


    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.graphics().camera().setZoom(3);

        engine.environment()
            .enableAllFeatures()
            .addSystem(new ClothRenderSystem())
            .addSystem(new InteractionSystem())
            .addSystem(new DebugSoftPhysicsSystem())
            .addEntity(new GravityComponent(Vector.y(800)))
            .addSystem(Order.DEBUG_OVERLAY, e -> {

                if (e.keyboard().isPressed(Key.ENTER)) {
                    List<Entity> cloth = ClothPrototype.createCloth(Bounds.atOrigin(e.mouse().position().snap(16), 64, 64), 8, e.environment());
//                    cloth.get(12).add(new CursorAttachmentComponent($(0, 32)));
//                    cloth.getLast().add(new CursorAttachmentComponent($(0, -32)));
//                    cloth.forEach(x -> x.add(new ChaoticMovementComponent(50, Duration.ofMillis(250))));
                    cloth.forEach(x -> x.get(PhysicsComponent.class).gravityModifier = 0.0);
                    cloth.forEach(x -> x.get(PhysicsComponent.class).friction = 2.0);
//                    cloth.forEach(x -> x.resize(4, 4));
                    e.environment().addEntities(cloth);
                }
            });

        engine.start();
    }

}