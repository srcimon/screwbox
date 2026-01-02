package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.SoftPhysicsSupport;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.RenderComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.SpriteBundle;

import java.util.List;

public class PlaygroundApp {

    static  private Entity grabbed;

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        List<Entity> rope = SoftPhysicsSupport.createRope(Line.between(Vector.of(4, 10), Vector.of(30, 30)), 10, engine.environment());
        rope.getFirst().add(new RopeRenderComponent(Color.ORANGE, 4)).add(new RenderComponent(SpriteBundle.DOT_WHITE));
rope.forEach(r -> r.get(PhysicsComponent.class).friction = 4);
        engine.graphics().camera().setPosition(Vector.of(4, 10));
        engine.environment().addEntities(rope);
        engine.environment().enableAllFeatures();

        engine.environment().addSystem(s -> {
            if(engine.mouse().isPressedLeft()) {
                engine.navigation().searchAtPosition(engine.mouse().position())
                    .checkingFor(Archetype.ofSpacial(PhysicsComponent.class))
                    .selectAll().forEach(e -> grabbed = e);
            }

            if(grabbed != null) {
                grabbed.get(PhysicsComponent.class).velocity = Vector.zero();
                grabbed.moveTo(engine.mouse().position());
            }
            if(!engine.mouse().isDownLeft()) {
                grabbed = null;
            }
            engine.graphics().camera().move(engine.keyboard().wsadMovement(engine.loop().delta(300)));
        });
        engine.start();
    }

}