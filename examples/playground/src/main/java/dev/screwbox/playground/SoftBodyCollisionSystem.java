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
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.OvalDrawOptions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static dev.screwbox.core.environment.Order.SIMULATION_PREPARE;

@ExecutionOrder(SIMULATION_PREPARE)
public class SoftBodyCollisionSystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftLinkComponent.class, SoftbodyCollisionComponent.class);

    record Check(Entity first, Entity second) {

    }

    record PointInPolygonCollision(Vector intruder, Line segment, Consumer<Vector> moveIntruder,
                                   Consumer<Vector> moveSegment) {
    }

    @Override
    public void update(Engine engine) {
        final var bodies = engine.environment().fetchAll(BODIES);
        final Set<Check> checks = initializeChecks(bodies);

        for (final var check : checks) {
            resolveBisectorIntrusion(engine, check.first, check.second);
            resolveBisectorIntrusion(engine, check.second, check.first);
            resolvePointInPolygonCollisions(engine, check.first, check.second);
            resolvePointInPolygonCollisions(engine, check.second, check.first);
        }
    }

    private void resolveBisectorIntrusion(Engine engine, Entity first, Entity second) {
        var firstPolygon = toPolygon(first);
        for(int i = 0; i < firstPolygon.nodeCount(); i++) {

            var ray = firstPolygon.bisectorRayOfNode(i);
            ray = Line.between(ray.start(), Vector.$((ray.end().x() + ray.start().x()) / 2.0, (ray.end().y() + ray.start().y()) / 2.0));
            for(var segment : toPolygon(second).segments()) {
                var intersection = ray.intersectionPoint(segment);
                if(intersection != null) {
                    first.get(SoftBodyComponent.class).nodes.get(i).moveTo(intersection);
                }
            }
            engine.graphics().world().drawLine(ray, LineDrawOptions.color(Color.MAGENTA).strokeWidth(2).drawOrder(9999999));
        }
       // engine.graphics().world().drawCircle(firstPolygon.center(),4, OvalDrawOptions.filled(Color.MAGENTA).drawOrder(9999999));
    }


    private static void resolvePointInPolygonCollisions(Engine engine, Entity firstEntity, Entity secondEntity) {
        Polygon first = toPolygon(firstEntity);
        Polygon second = toPolygon(secondEntity);
        for (int z = 0; z < first.definitionNotes().size(); z++) {
            final var node = first.definitionNotes().get(z);
            if (second.contains(node)) {
                Line closest = second.segments().getFirst();
                double distance = closest.closestPoint(node).distanceTo(node);
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




                var body = secondEntity.get(SoftBodyComponent.class);
                final var fn = body.nodes.get(segmentNr);
                final var fn2 = body.nodes.get(segmentNr + 1);
                final var intruderNr = z;
                var collision = new PointInPolygonCollision(node, closest,
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
            //    engine.graphics().world().drawCircle(collision.intruder, 2, OvalDrawOptions.filled(Color.MAGENTA).drawOrder(Order.DEBUG_OVERLAY.drawOrder()));
             //   engine.graphics().world().drawCircle(closestPointToIntruder, 2, OvalDrawOptions.filled(Color.WHITE).drawOrder(Order.DEBUG_OVERLAY.drawOrder()));
//                engine.graphics().world().drawLine(node, node.add(delta), LineDrawOptions.color(Color.WHITE).strokeWidth(2).drawOrder(Order.DEBUG_OVERLAY.drawOrder()));
//                engine.graphics().world().drawLine(collision.segment, LineDrawOptions.color(Color.MAGENTA).strokeWidth(3).drawOrder(Order.DEBUG_OVERLAY.drawOrder()));
            }
        }
    }

    private static Polygon toPolygon(Entity entity) {
        return Polygon.ofNodes(entity.get(SoftBodyComponent.class).nodes.stream().map(Entity::position).toList());
    }

    private static Set<Check> initializeChecks(List<Entity> bodies) {
        final Set<Check> checks = new HashSet<>();
        for (final var body : bodies) {
            for (final var other : bodies) {
                if (!body.equals(other) && !checks.contains(new Check(other, body))) {
                    checks.add(new Check(body, other));
                }
            }
        }
        return checks;
    }
}
