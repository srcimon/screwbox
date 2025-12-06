package dev.screwbox.playground;

import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.OvalDrawOptions;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static dev.screwbox.core.environment.Order.SIMULATION_PREPARE;

@ExecutionOrder(SIMULATION_PREPARE)
public class SoftBodyCollisionSystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftLinkComponent.class, SoftbodyCollisionComponent.class);

    record Check(Entity first, Entity second) {

    }

    @Override
    public void update(Engine engine) {
        final var bodies = engine.environment().fetchAll(BODIES);
        final Set<Check> checks = new HashSet<>();
        for (final var body : bodies) {
            for (final var other : bodies) {
                if (!body.equals(other) && !checks.contains(new Check(other, body))) {
                    checks.add(new Check(body, other));
                }
            }
        }

        for (final var check : checks) {
            drawInsidePoints(engine, check.first, check.second);
            drawInsidePoints(engine, check.second, check.first);
        }
    }

    record PolygonCollision(Vector intruder, Line segment, Consumer<Vector> moveIntruder,
                            Consumer<Vector> moveSegment) {

    }

    private static void drawInsidePoints(Engine engine, Entity firstEntity, Entity secondEntity) {
        Polygon first = toPolygon(firstEntity);
        Polygon second = toPolygon(secondEntity);
        for (int z = 0; z < first.nodes().size(); z++) {
            final var node = first.nodes().get(z);
            if (second.contains(node)) {
                Line closest = second.segments().getFirst();
                double distance = closest.closestPoint(node).distanceTo(node);//TODO new method -> closestDistance
                int segmentNr = 0;
                for (int i = 1; i < second.segments().size(); i++) {
                    var segment = second.segments().get(i);
                    double currentDistance = segment.closestPoint(node).distanceTo(node);
                    if (currentDistance < distance) {
                        closest = segment;
                        distance = currentDistance;
                        segmentNr = i;
                    }
                }




                //TODO Resolve all collisions after detection, using the accumulated data to apply positional corrections and calculate impulses.
                var body = secondEntity.get(SoftBodyComponent.class);
                final var fn = body.nodes.get(segmentNr);
                final var fn2 = body.nodes.get(segmentNr + 1);
                final var intruderNr = z;
                var collision = new PolygonCollision(node, closest,
                        p -> {
                            Entity entity = firstEntity.get(SoftBodyComponent.class).nodes.get(intruderNr);
                            entity.moveBy(p);
                            entity.get(PhysicsComponent.class).velocity = entity.get(PhysicsComponent.class).velocity.add(p.multiply(10 * engine.loop().delta()));
                        },
                        p -> {
                            fn.moveBy(p);
                            fn.get(PhysicsComponent.class).velocity = fn.get(PhysicsComponent.class).velocity.add(p.multiply(10 * engine.loop().delta()));
                            fn2.moveBy(p);
                            fn2.get(PhysicsComponent.class).velocity = fn2.get(PhysicsComponent.class).velocity.add(p.multiply(10 * engine.loop().delta()));
                        });
                Vector closestPointToIntruder = closest.closestPoint(collision.intruder);
                Vector delta = closestPointToIntruder.substract(collision.intruder);
                collision.moveIntruder.accept(delta.multiply(0.5));
                first = toPolygon(firstEntity);
                second = toPolygon(secondEntity);
                collision.moveSegment.accept(delta.multiply(-0.5));
//                engine.graphics().world().drawCircle(collision.intruder, 2, OvalDrawOptions.filled(Color.MAGENTA).drawOrder(Order.DEBUG_OVERLAY.drawOrder()));
//                engine.graphics().world().drawCircle(closestPointToIntruder, 2, OvalDrawOptions.filled(Color.WHITE).drawOrder(Order.DEBUG_OVERLAY.drawOrder()));
//                engine.graphics().world().drawLine(node, node.add(delta), LineDrawOptions.color(Color.WHITE).strokeWidth(2).drawOrder(Order.DEBUG_OVERLAY.drawOrder()));
//                engine.graphics().world().drawLine(collision.segment, LineDrawOptions.color(Color.MAGENTA).strokeWidth(3).drawOrder(Order.DEBUG_OVERLAY.drawOrder()));
            }
        }
    }

    private static Polygon toPolygon(Entity entity) {
        return Polygon.ofNodes(entity.get(SoftBodyComponent.class).nodes.stream().map(Entity::position).toList());
    }
}
