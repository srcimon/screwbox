package dev.screwbox.playground;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.ScrewBox;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.core.LogFpsSystem;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.GravityComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyCollisionComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyShapeComponent;
import dev.screwbox.core.environment.softphysics.SoftPhysicsSupport;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.playground.misc.DebugSoftPhysicsSystem;
import dev.screwbox.playground.misc.InteractionSystem;

import java.util.List;

public class PlaygroundApp {

    static Vector pos;

    public static void main(String[] args) {
        Engine engine = ScrewBox.createEngine("Playground");

        engine.graphics().camera().setZoom(4);

        engine.environment()
            .enableAllFeatures()
            .addSystem(new LogFpsSystem())
            .addSystem(new InteractionSystem())
            .addEntity(new Entity().add(new ColliderComponent()).bounds(engine.graphics().visibleArea().moveBy(0, engine.graphics().visibleArea().height())))
            .addSystem(new DebugSoftPhysicsSystem())
            .addEntity(new GravityComponent(Vector.$(0, 500)))
            .addSystem(Order.DEBUG_OVERLAY, e -> {
                if (e.mouse().isDownRight() && pos == null) {
                    pos = e.mouse().position();
                } else if (!e.mouse().isDownRight() && pos != null) {
                    Bounds around = Bounds.around(List.of(pos, e.mouse().position()));
                    var box = SoftPhysicsSupport.createBox(around, e.environment());
                    box.forEach(b -> b.get(PhysicsComponent.class).friction = 2);
                    box.root().add(new SoftBodyRenderComponent(Color.RED));
                    box.root().add(new SoftBodyCollisionComponent());
                    engine.environment().addEntities(box);
                    pos = null;
                }
                if(pos != null) {
                    e.graphics().world().drawRectangle( Bounds.around(List.of(pos, e.mouse().position())), RectangleDrawOptions.outline(Color.WHITE));
                }
            });

        engine.start();
    }

}