package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.ChaoticMovementComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.ClothRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;
import dev.screwbox.core.environment.softphysics.SoftPhysicsSupport;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.playground.misc.InteractionSystem;

public class PlaygroundApp {

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.graphics().camera().setZoom(4);

        engine.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(new InteractionSystem())
//            .addSystem(new DebugSoftPhysicsSystem())
            .addEntity(new GravityComponent(Vector.$(0, 500)))
            .addSystem(Order.DEBUG_OVERLAY, e -> {


                if (e.keyboard().isPressed(Key.ENTER)) {
                    var cloth = SoftPhysicsSupport.createCloth(Bounds.atOrigin(e.mouse().position(), 128, 64), Size.of(30, 15), e.environment());
                    cloth.root().add(new ClothRenderComponent(), x -> {
                        x.texture = SpriteBundle.MARKER_CROSSHAIR.get();
                    });
                    cloth.forEach(entity -> entity.get(PhysicsComponent.class).gravityModifier = 0.4);
                    cloth.forEach(entity -> entity.get(PhysicsComponent.class).friction = 4.5);
                    cloth.forEach(entity -> entity.add(new ChaoticMovementComponent(100, Duration.ofMillis(200))));
                    cloth.forEach(entity -> entity.resize(4, 4));
                    cloth.forEach(entity -> {
                        var structure = entity.get(SoftStructureComponent.class);
                        if (structure != null) {
                            structure.expand = 200;
                            structure.flexibility = 200;
                            structure.retract = 100;
                        }
                    });
                    cloth.outlineTop().forEach(entity -> entity.remove(PhysicsComponent.class));
                    e.environment().addEntities(cloth);
                }

            });

        engine.start();
    }

}