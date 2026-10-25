package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.rendering.CameraTargetComponent;
import dev.screwbox.core.environment.softphysics.RopeComponent;
import dev.screwbox.core.environment.softphysics.RopeRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftPhysicsSupport;
import dev.screwbox.core.graphics.Sprite;

public class PlaygroundApp {


    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Playground");
        screwBox.graphics().camera().setZoom(3);
        var snake = SoftPhysicsSupport.createRope(Vector.$(0, 0), Vector.$(256, 0), 9, screwBox.environment());
        snake.root().add(new RopeRenderComponent(Sprite.fromFile("snake.png")));
        snake.root().add(new CameraTargetComponent());
        snake.forEach(s -> s.get(PhysicsComponent.class).friction = 0.8);
        screwBox.environment().enableAllFeatures().addEntities(snake)
            .addSystem(new DebugGridSystem(16, 256))
            .addSystem(s -> {
                var phys = s.environment().fetchSingleton(Archetype.ofSpacial(CameraTargetComponent.class)).get(PhysicsComponent.class);
                phys.velocity = s.keyboard().wsadMovement(80);
            })
            .addSystem(s -> {
                for (var body : s.environment().fetchAllHaving(RopeComponent.class)) {
                    var nodes = body.get(RopeComponent.class).nodes;
                    for (int i = 0; i < nodes.size() - 1; i++) {
                        Vector newpos = Line.between(nodes.get(i).position(), nodes.get(i + 1).position()).length(32).end();
                        nodes.get(i + 1).moveTo(newpos);
                    }
                }
            });
        screwBox.start();
    }
}