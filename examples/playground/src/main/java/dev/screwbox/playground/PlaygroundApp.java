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
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.softphysics.ClothEntities;
import dev.screwbox.core.environment.softphysics.RopeEntities;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftPhysicsSupport;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;
import dev.screwbox.core.graphics.SpriteBundle;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.playground.misc.DebugSoftPhysicsSystem;
import dev.screwbox.playground.misc.InteractionSystem;

public class PlaygroundApp {

    // Rope: root, all, end, knots
    // SoftBody root, all, unstableKnots, stabilizedKnots
    // Box: root, all, edges, leftBottom, rightBottom, rightTop, outline, knots, rightBorder, leftBorder, topBorder, bottomBorder
    // Cloth root, all, edges, leftBottom, rightBottom, rightTop, outline, knots, rightBorder, leftBorder, topBorder, bottomBorder

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.graphics().camera().setZoom(4);

        engine.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(new ClothRenderSystem())
            .addSystem(new InteractionSystem())
//            .addSystem(new DebugSoftPhysicsSystem())
            .addEntity(new GravityComponent(Vector.$(-400, 500)))
            .addSystem(Order.DEBUG_OVERLAY, e -> {


                if (e.keyboard().isPressed(Key.ENTER)) {
                    ClothEntities cloth = SoftPhysicsSupport.createCloth(Bounds.atOrigin(e.mouse().position(), 128, 64), Size.of(30, 15), e.environment());
//                        .root(root -> root.add(new SoftBodyRenderComponent(Color.TRANSPARENT), r -> {
//                            r.rounded = false;
//                            r.outlineColor = Color.WHITE;
//                            r.outlineStrokeWidth = 4;
//                        }))
                    cloth.root().add(new ClothRenderComponent());
                    cloth.all().forEach(entity -> entity.get(PhysicsComponent.class).gravityModifier = 0.4);
                    cloth.all().forEach(entity -> entity.get(PhysicsComponent.class).friction = 4.5);
                    cloth.all().forEach(entity -> entity.add(new ChaoticMovementComponent(100, Duration.ofMillis(200))));
                    cloth.all().forEach(entity -> entity.resize(4, 4));
                    cloth.all().forEach(entity -> {
                        var structure = entity.get(SoftStructureComponent.class);
                        if (structure != null) {
                            structure.expand = 200;
                            structure.flexibility = 400;
                            structure.retract = 160;
                        }
                    });
                    cloth.outlineTop().forEach(entity -> entity.remove(PhysicsComponent.class));
                    e.environment().addEntities(cloth.all());
                }

            });

        engine.start();
    }

}