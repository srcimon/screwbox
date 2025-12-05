package dev.screwbox.playground.softbody;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;

import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExecutionOrder(Order.PREPARATION)//Before SIMULATION_EARLY
public class SoftBodyCollisionSystemV1 implements EntitySystem {

    private static final Archetype SOFTBODIES = Archetype.ofSpacial(SoftbodyComponent.class, SoftLinkComponent.class);

    record Item(Entity entity, Area area, List<Entity> nodes) {

    }

    record Pair(Entity entity, Entity target) {

    }

    @Override
    public void update(Engine engine) {
        Set<Pair> done = new HashSet<>();
        var items = engine.environment().fetchAll(SOFTBODIES).stream().map(b -> new Item(b, toArea(b), b.get(SoftbodyComponent.class).nodes)).toList();
        for (var item : items) {
            for (var target : items) {
                if (item != target) {
                    Area clone = new Area(item.area);
                    clone.intersect(target.area);
                    boolean collided = !clone.isEmpty();
                    if (collided && !done.contains(new Pair(item.entity, target.entity)) && !done.contains(new Pair(target.entity, item.entity))) {
                        done.add(new Pair(item.entity, target.entity));
                        // engine.graphics().world().drawRectangle(collisionBounds, RectangleDrawOptions.filled(Color.BLUE).drawOrder(Order.PRESENTATION_UI.drawOrder()));
                        extracted(engine, item, target, clone);
                        //TODO fetch max resolve vector and move whole structure
                        extracted(engine, target, item, clone);
                    }
                }
            }
        }
    }

    private void extracted(Engine engine, Item item, Item target, Area clone) {
        //TODO add config to component
        int motitionConfig = 550;
        Percent submotion = Percent.of(0.6);
        Percent move = Percent.of(0.5);
        Percent accelerate = Percent.of(1);


        List<Entity> contained = new ArrayList<>();
        for (final var node : item.nodes) {
            if (clone.contains(node.position().x(), node.position().y())) {
                contained.add(node);
            }
        }
        if (contained.isEmpty()) {
            return;
        }
        Vector center = center(target.nodes);
        for (final var node : item.nodes) {
            Vector motion = node.position().substract(center).length(1).multiply(engine.loop().delta() * motitionConfig);
            Vector multiply = motion.multiply(contained.contains(node) ? 1 : submotion.value());
            node.moveBy(multiply.multiply(move.value()));
            node.get(PhysicsComponent.class).velocity = node.get(PhysicsComponent.class).velocity.add(multiply.multiply(accelerate.value()));
        }

    }

    private Vector center(List<Entity> entities) {
        Vector c = Vector.zero();
        for (final var e : entities) {
            c = Vector.of((c.x() + e.position().x()) / 2.0, (c.y() + e.position().y()) / 2.0);
        }
        return c;
    }

    private Area toArea(Entity b) {
        Polygon poly = new Polygon();
        b.get(SoftbodyComponent.class).nodes.stream().forEach(n -> poly.addPoint((int) n.position().x(), (int) n.position().y()));
        return new Area(poly);
    }
}
