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
        for(var item : items) {
            for(var target : items) {
                if(item!=target) {
                    Area clone = new Area(item.area);
                    clone.intersect(target.area);
                    boolean collided = !clone.isEmpty();
                    if(collided && !done.contains(new Pair(item.entity, target.entity)) && !done.contains(new Pair(target.entity, item.entity))) {
                        done.add(new Pair(item.entity, target.entity));
                        Bounds collisionBounds = Bounds.atOrigin(clone.getBounds().x, clone.getBounds().y, clone.getBounds().width, clone.getBounds().height);
                       // engine.graphics().world().drawRectangle(collisionBounds, RectangleDrawOptions.filled(Color.BLUE).drawOrder(Order.PRESENTATION_UI.drawOrder()));
                        for(final var node : item.nodes) {
                            Vector resolveVector = getResolveVector(collisionBounds, node.bounds());
                            if(collisionBounds.contains(node.position())) {
                                node.moveBy(resolveVector);
                            }
                        }
                        for(final var node : target.nodes) {
                            Vector resolveVector = getResolveVector(collisionBounds, node.bounds());
                            if(collisionBounds.contains(node.position())) {
                                node.moveBy(resolveVector.invert());
                            }
                        }
                    }
                }
            }
        }
    }

    private Area toArea(Entity b) {
        Polygon poly = new Polygon();
        b.get(SoftbodyComponent.class).nodes.stream().forEach(n -> poly.addPoint((int)n.position().x(), (int)n.position().y()));
        return new Area(poly);
    }

    private static Vector getResolveVector(final Bounds colliderBounds, Bounds entityBounds) {

        final boolean colliderBelowPhysics = abs(colliderBounds.maxY() - entityBounds.minY()) < abs(
                colliderBounds.minY() - entityBounds.maxY());

        final double deltaY = colliderBelowPhysics
                ? colliderBounds.maxY() - entityBounds.minY()
                : colliderBounds.minY() - entityBounds.maxY();

        final boolean colliderLeftOfPhysics = abs(colliderBounds.maxX() - entityBounds.minX()) < abs(
                colliderBounds.minX() - entityBounds.maxX());

        final double deltaX = colliderLeftOfPhysics
                ? colliderBounds.maxX() - entityBounds.minX()
                : colliderBounds.minX() - entityBounds.maxX();

        return abs(deltaY) < abs(deltaX)
                ? Vector.y(deltaY)
                : Vector.x(deltaX);
    }
}
