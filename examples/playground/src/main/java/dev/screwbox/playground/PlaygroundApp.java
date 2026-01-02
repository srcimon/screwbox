package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.graphics.Color;

import static dev.screwbox.core.Vector.$;

public class PlaygroundApp {


    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");


        var rope = Rope.createRope(Line.between($(4, 10), $(30, 130)), 8, engine.environment())
            .customizeRender(new RopeRenderComponent(Color.ORANGE, 4))
            .nodeSize(4)
            .friction(2);

        engine.environment()
            .enableAllFeatures()
            .addSystem(new InteractionSystem())
            .addEntities(rope.entities());

        engine.start();
    }

}