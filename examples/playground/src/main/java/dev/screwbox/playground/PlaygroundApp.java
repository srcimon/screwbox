package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyCollisionComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyShapeComponent;
import dev.screwbox.core.graphics.Color;

import java.util.List;

import static dev.screwbox.core.Vector.$;

public class PlaygroundApp {


    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");


        engine.environment()
            .enableAllFeatures()
            .addSystem(new InteractionSystem())
            .addSystem(new DebugSoftPhysicsSystem())
            .addSystem(e -> {
                if(e.mouse().isPressedRight()) {
                    var softBody = createSoftBody(engine.mouse().position(), engine);
                    e.environment().addEntities(softBody);
                }
            })
            .addEntities(createRope(engine));

        engine.start();
    }

    private static List<Entity> createSoftBody(Vector position, Engine engine) {
        List<Vector> positions = List.of($(30, 10), $(80, 30), $(80, 89), $(20, 160));
        var updated = positions.stream().map(p -> p.add(position)).toList();
        var softBody = SoftPhysicsSupport.createStabilizedSoftBody(updated, engine.environment());
        softBody.forEach(node -> node.get(PhysicsComponent.class).friction = 2);
        softBody.forEach(node -> node.resize(4, 4));
        softBody.getFirst()
            .add(new SoftBodyRenderComponent(Color.YELLOW.opacity(0.3)), r -> r.rounded = false)
            .add(new SoftBodyCollisionComponent())
            .add(new SoftBodyShapeComponent());
        return softBody;
    }

    private static List<Entity> createRope(Engine engine) {
        var rope = SoftPhysicsSupport.createRope($(4, 10), $(30, 130), 8, engine.environment());

        rope.getFirst().add(new RopeRenderComponent(Color.MAGENTA, 2));
        rope.forEach(node -> node.resize(4, 4));
        rope.forEach(node -> node.get(PhysicsComponent.class).friction = 1);
        rope.getFirst().remove(PhysicsComponent.class);
        return rope;
    }

}