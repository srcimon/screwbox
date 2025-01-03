package io.github.srcimon.screwbox.playgrounds.playercontrolls;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.ScrewBox;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.environment.logic.StateComponent;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.GravityComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;
import io.github.srcimon.screwbox.core.graphics.Color;
import io.github.srcimon.screwbox.core.graphics.drawoptions.RectangleDrawOptions;

//TODO add playgrounds to readme.md
public class PlayerControllsApp {

    public static void main(String[] args) {
        Engine screwBox = ScrewBox.createEngine("Player Controlls");
screwBox.graphics().camera().setZoom(2);
        screwBox.environment()
                .enableAllFeatures()
                .addSystem(engine -> {
                    for(var entity : engine.environment().fetchAll(Archetype.ofSpacial())) {
                        engine.graphics().world().drawRectangle(entity.bounds(), RectangleDrawOptions.filled(Color.WHITE));
                    }
                })
                .addEntity("gravity",
                        new GravityComponent(Vector.y(500)))
                .addEntity("player",
                        new TransformComponent(0, 0, 10, 24),
                        new PhysicsComponent(),
                        new StateComponent(new StandingState()))
                .addEntity("floor",
                        new TransformComponent(-150, 24, 600, 16),
                        new ColliderComponent());

        screwBox.start();
    }
}