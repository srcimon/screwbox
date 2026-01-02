package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.graphics.Color;

import java.util.List;

import static dev.screwbox.core.Vector.$;

public class PlaygroundApp {


    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");


        List<Entity> rope = SoftPhysicsSupport.createRope(Line.between($(4, 10), $(30, 130)), 8, engine.environment());
        rope.getFirst().add(new RopeRenderComponent(Color.ORANGE, 4));

        engine.environment()
            .enableAllFeatures()
            .addSystem(new InteractionSystem())
            .addEntities(rope);

        engine.start();
    }

}