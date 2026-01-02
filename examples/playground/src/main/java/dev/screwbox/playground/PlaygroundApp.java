package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SoftPhysicsSupport;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Size;

import java.util.List;

import static dev.screwbox.core.Vector.$;

public class PlaygroundApp {

    static private Entity grabbed;

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        var ropeOptions = SoftPhysicsSupport.RopeOptions.nodeCount(10)
            .fixedEnd()
            .nodeSize(Size.square(4))
            .friction(3);

        List<Entity> rope = SoftPhysicsSupport.createRope(Line.between($(4, 10), $(30, 130)), ropeOptions, engine.environment());
        rope.getFirst().add(new RopeRenderComponent(Color.ORANGE, 4));

        engine.environment().addEntities(rope);
        engine.environment().enableAllFeatures();

        engine.environment().addSystem(s -> {
            if (engine.mouse().isPressedLeft()) {
                engine.navigation().searchAtPosition(engine.mouse().position())
                    .checkingFor(Archetype.ofSpacial(PhysicsComponent.class))
                    .selectAll().forEach(e -> grabbed = e);
            }

            if (grabbed != null) {
                grabbed.get(PhysicsComponent.class).velocity = Vector.zero();
                grabbed.moveTo(engine.mouse().position());
            }
            if (!engine.mouse().isDownLeft()) {
                grabbed = null;
            }
            engine.graphics().camera().move(engine.keyboard().wsadMovement(engine.loop().delta(300)));
        });
        engine.start();
    }

}