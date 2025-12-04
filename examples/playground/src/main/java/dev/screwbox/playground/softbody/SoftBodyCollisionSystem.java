package dev.screwbox.playground.softbody;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.utils.internal.CollisionCheck;
import dev.screwbox.core.utils.internal.CollisionResolver;

import java.awt.*;
import java.awt.geom.Area;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Math.abs;

@ExecutionOrder(Order.PREPARATION)//Before SIMULATION_EARLY
public class SoftBodyCollisionSystem implements EntitySystem {

    private static final Archetype SOFTBODIES = Archetype.ofSpacial(SoftbodyComponent.class, SoftLinkComponent.class);
    record Item(Entity entity, Area area, List<Entity> nodes) {

    }

    record Pair(Entity entity, Entity target) {

    }
    @Override
    public void update(Engine engine) {
        Set<Pair> done = new HashSet<>();
        var items = engine.environment().fetchAll(SOFTBODIES).stream().map(b -> new Item(b, toArea(b), b.get(SoftbodyComponent.class).nodes)).toList();
        for(int i = 0; i < 4; i++) {
            for (var item : items) {
                for (var target : items) {
                    if (item != target) {
                        Area clone = new Area(item.area);
                        clone.intersect(target.area);
                        boolean collided = !clone.isEmpty();
                        if (collided && !done.contains(new Pair(item.entity, target.entity)) && !done.contains(new Pair(target.entity, item.entity))) {
                            done.add(new Pair(item.entity, target.entity));
                            // engine.graphics().world().drawRectangle(collisionBounds, RectangleDrawOptions.filled(Color.BLUE).drawOrder(Order.PRESENTATION_UI.drawOrder()));
                            for (final var node : item.nodes) {
                                Vector center = center(target.nodes);
                                if (clone.contains(node.position().x(), node.position().y())) {
                                    Vector motion = node.position().substract(center).multiply(engine.loop().delta() * 40);
                                    node.moveBy(motion);
                                }
                            }
                            //TODO fetch max resolve vector and move whole structure
                            for (final var node : target.nodes) {
                                Vector center = center(item.nodes);
                                if (clone.contains(node.position().x(), node.position().y())) {
                                    Vector motion = node.position().substract(center).multiply(engine.loop().delta() * 40);
                                    node.moveBy(motion);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Vector center(List<Entity> entities) {
        Vector c = Vector.zero();
        for(final var e : entities) {
            c = Vector.of((c.x() + e.position().x()) / 2.0, (c.y() + e.position().y()) / 2.0);
        }
        return c;
    }
    private Area toArea(Entity b) {
        Polygon poly = new Polygon();
        b.get(SoftbodyComponent.class).nodes.stream().forEach(n -> poly.addPoint((int)n.position().x(), (int)n.position().y()));
        return new Area(poly);
    }
}
