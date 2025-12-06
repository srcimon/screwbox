package dev.screwbox.playground.builder;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.environment.fluids.FloatComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyRenderComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.environment.softphysics.SoftStructureComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.PolygonDrawOptions;
import dev.screwbox.core.keyboard.Key;
import dev.screwbox.playground.SoftbodyCollisionComponent;

import java.util.ArrayList;
import java.util.List;

@ExecutionOrder(Order.OPTIMIZATION)
public class DynamicCreationSystem implements EntitySystem {

    static final List<Vector> points = new ArrayList<>();

    @Override
    public void update(Engine engine) {
        if(points.size() > 0) {
            engine.graphics().world().drawPolygon(points, PolygonDrawOptions.outline(Color.WHITE).drawOrder(Order.DEBUG_OVERLAY_LATE.drawOrder()));
        }
        var environment = engine.environment();
        if (engine.mouse().isPressedRight()) {
            points.add(engine.mouse().position());
        }
        if (engine.keyboard().isPressed(Key.SPACE)) {
            int firstId = environment.peekId();
            final var entities = new ArrayList<Entity>();
            for (int i = 0; i < points.size(); i++) {
                var p = points.get(i);
                entities.add(new Entity(environment.allocateId())
                        .add(i == points.size() - 1 ? new SoftLinkComponent(firstId) : new SoftLinkComponent(environment.peekId()))
                        .add(new TransformComponent(p, 2, 2))
                        .add(new PhysicsComponent(), y -> {
                            y.friction = 2;
                        })
                        .add(new FloatComponent())
                );
            }
            entities.get(0)
                    .add(new SoftBodyRenderComponent(Color.ORANGE.opacity(0.5)), xx -> xx.outlineColor = Color.ORANGE)
                    .add(new SoftBodyComponent())
                    .add(new SoftbodyCollisionComponent());


            for (int i = 0; i < entities.size(); i++) {
                int index = (i + 4) % entities.size();
                int index2 = (i + 3) % entities.size();
                int i1 = entities.get(index).id().get();
                List<Integer> linked = new ArrayList<>();

                if (i1 != entities.get(i).id().get()) {
                    linked.add(i1);
                }
                Integer i2 = entities.get(index2).id().get();
                if (i2 != entities.get(i).id().get()) {
                    linked.add(i2);
                }
                entities.get(i).add(new SoftStructureComponent(linked));
            }
            environment.addEntities(entities);
            points.clear();
        }
    }
}
