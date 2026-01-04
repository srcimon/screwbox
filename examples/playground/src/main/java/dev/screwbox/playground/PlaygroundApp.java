package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.ChaoticMovementComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.keyboard.Key;

import java.util.List;

import static dev.screwbox.core.Vector.$;

public class PlaygroundApp {


    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.graphics().camera().setZoom(4);

        engine.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(new ClothRenderSystem())
            .addSystem(new InteractionSystem())
//            .addSystem(new DebugSoftPhysicsSystem())
            .addEntity(new GravityComponent($(-400, 600)))
            .addSystem(Order.DEBUG_OVERLAY, e -> {

                if (e.keyboard().isPressed(Key.ENTER)) {
                    int size = 8;
                    List<Entity> cloth = ClothPrototype.createCloth(Bounds.atOrigin(e.mouse().position().snap(16), 64, 64), size, e.environment());
                    cloth.getFirst().add(new CursorAttachmentComponent($(0, 32)));
                    cloth.get(64/size/2-1).add(new CursorAttachmentComponent($(0, 0)));
                    cloth.get(64/size-1).add(new CursorAttachmentComponent($(0, -32)));
                    cloth.forEach(x -> x.add(new ChaoticMovementComponent(80, Duration.ofMillis(250))));
//                    cloth.getFirst().add(new RenderComponent(SpriteBundle.DOT_WHITE.get().scaled(0.5)));
                    cloth.forEach(x -> x.get(PhysicsComponent.class).gravityModifier = 0.3);
                    cloth.forEach(x -> x.get(PhysicsComponent.class).friction = 1.0);
                    cloth.forEach(x -> x.resize(4, 4));
                    e.environment().addEntities(cloth);
                }
            });

        engine.start();
    }

}