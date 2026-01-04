package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.SoftPhysicsSupport;
import dev.screwbox.core.environment.physics.ChaoticMovementComponent;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.CursorAttachmentComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyCollisionComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyShapeComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.keyboard.Key;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.Vector.$;

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
                if (e.keyboard().isPressed(Key.SPACE)) {
                    pppp.add(e.mouse().position());
                }

                if (e.keyboard().isPressed(Key.ENTER)) {
                    List<Entity> cloth = ClothPrototype.createCloth(Bounds.atOrigin(e.mouse().position().snap(16), 64, 64), 16, e.environment());
//                    cloth.get(12).add(new CursorAttachmentComponent($(0, 32)));
//                    cloth.getLast().add(new CursorAttachmentComponent($(0, -32)));
                    cloth.forEach(x -> x.add(new ChaoticMovementComponent(10, Duration.ofMillis(250))));
                    cloth.forEach(x -> x.get(PhysicsComponent.class).gravityModifier = 0.0);
                    cloth.forEach(x -> x.get(PhysicsComponent.class).friction = 2.0);
                    cloth.forEach(x -> x.resize(4, 4));
                    e.environment().addEntities(cloth);
                }
                if (e.mouse().isPressedRight()) {
                    var softBody = createSoftBody(engine);
                    e.environment().addEntities(softBody);
                }
            })
            .addEntity(new Entity().bounds(Bounds.atOrigin(-800, 300, 1600, 300)).add(new ColliderComponent()))
            .addEntities(createRope(engine));

        engine.start();
    }

    static List<Vector> pppp = new ArrayList<>();

    private static List<Entity> createSoftBody(Engine engine) {
        var softBody = SoftPhysicsSupport.createStabilizedSoftBody(Polygon.ofNodes(pppp), engine.environment());
        pppp.clear();
        softBody.forEach(node -> node.get(PhysicsComponent.class).friction = 1);
        softBody.forEach(node -> node.resize(8, 8));
        softBody.getFirst()
            .add(new SoftBodyRenderComponent(Color.YELLOW.opacity(0.3)), r -> r.rounded = false)
            .add(new SoftBodyCollisionComponent())
            .add(new SoftBodyShapeComponent());
        return softBody;
    }

    private static List<Entity> createRope(Engine engine) {
        var rope = SoftPhysicsSupport.createRope($(4, 10), $(4, 50), 8, engine.environment());

        rope.getFirst().add(new RopeRenderComponent(Color.MAGENTA, 2));
        rope.forEach(node -> node.resize(4, 4));
        rope.forEach(node -> node.tryGet(SoftLinkComponent.class).ifPresent(link -> link.flexibility = 200));
        rope.forEach(node -> node.get(PhysicsComponent.class).friction = 1);
        rope.getFirst().remove(PhysicsComponent.class);
        return rope;
    }

}