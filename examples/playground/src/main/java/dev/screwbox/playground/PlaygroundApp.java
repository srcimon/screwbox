package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.graphics.Color;

import java.util.List;

import static dev.screwbox.core.Vector.$;

public class PlaygroundApp {


    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.environment()
            .enableAllFeatures()
            .addSystem(new InteractionSystem())
            .addEntities(createRope(engine));

        engine.start();
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