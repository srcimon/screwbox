package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SoftPhysicsSupport;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;

import java.util.List;

import static dev.screwbox.core.Vector.$;

public class PlaygroundApp {


    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        var ropeOptions = SoftPhysicsSupport.RopeOptions.nodeCount(10)
            .fixedEnd()
            .nodeSize(Size.square(4))
            .friction(3);

        List<Entity> rope = SoftPhysicsSupport.createRope(Line.between($(4, 10), $(30, 130)), ropeOptions, engine.environment());
        rope.getFirst().add(new RopeRenderComponent(Color.ORANGE, 4));

        engine.environment()
            .enableAllFeatures()
            .addSystem(new InteractionSystem())
            .addEntities(rope);

        engine.start();
    }

}