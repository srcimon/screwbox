package dev.screwbox.playground;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.environment.softphysics.SoftBodyComponent;
import dev.screwbox.core.environment.softphysics.SoftLinkComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static dev.screwbox.core.environment.Order.SIMULATION_PREPARE;

@ExecutionOrder(SIMULATION_PREPARE)
public class SoftBodyCollisionSystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftLinkComponent.class, SoftbodyCollisionComponent.class);

    private static class Check {

        private Polygon firstPolygon;
        private Polygon secondPolygon;
        private Entity first;
        private Entity second;

        public Check(Entity first, Entity second) {
            this.first = first;
            this.second = second;
            updateFirstPolygon();
            updateSecondPolygon();
        }

        void updateFirstPolygon() {
            this.firstPolygon = toPolygon(first);
        }

        void updateSecondPolygon() {
            this.secondPolygon = toPolygon(second);
        }

        public Check inverse() {
            return new Check(second, first);
        }

        public boolean isCandidate() {
            return firstPolygon.bounds().intersects(secondPolygon.bounds());
        }
    }

    record PointInPolygonCollision(Vector intruder, Line segment, Consumer<Vector> moveIntruder,
                                   Consumer<Vector> moveSegment) {
    }

    //TODO add actual collision information to component
    @Override
    public void update(final Engine engine) {
        Time t = Time.now();
        final var bodies = engine.environment().fetchAll(BODIES);
        final List<Check> checks = initializeChecks(bodies);
        for (final var check : checks) {
            final Check inverse = check.inverse();
            resolveBisectorIntrusion(check);
            resolveBisectorIntrusion(inverse);
            resolvePointInPolygonCollisions(engine, check);
            resolvePointInPolygonCollisions(engine, inverse);
        }
        System.out.println(Duration.since(t).nanos());
    }

    private void resolveBisectorIntrusion(Check check) {
        for (int i = 0; i < check.firstPolygon.nodeCount(); i++) {

            var rayo = check.firstPolygon.bisectorRay(i);
            if (rayo.isPresent()) {
                var ray = rayo.get();
                ray = Line.between(ray.start(), Vector.$((ray.end().x() + ray.start().x()) / 2.0, (ray.end().y() + ray.start().y()) / 2.0));
                for (var segment : toPolygon(check.second).segments()) {
                    var intersection = ray.intersectionPoint(segment);
                    if (intersection != null) {
                        Entity entity = check.first.get(SoftBodyComponent.class).nodes.get(i);
                        entity.moveTo(intersection);
                        entity.get(PhysicsComponent.class).velocity = Vector.zero();
                        check.updateFirstPolygon();
                    }

                }
            }
        }
    }


    private static void resolvePointInPolygonCollisions(Engine engine, Check check) {
        for (int z = 0; z < check.firstPolygon.definitionNotes().size(); z++) {
            final var node = check.firstPolygon.definitionNotes().get(z);
            if (check.secondPolygon.contains(node)) {
                Line closest = check.secondPolygon.segments().getFirst();
                double distance = closest.closestPoint(node).distanceTo(node);
                int segmentNr = 0;
                for (int i = 1; i < check.secondPolygon.segments().size(); i++) {
                    var segment = check.secondPolygon.segments().get(i);
                    double currentDistance = segment.closestPoint(node).distanceTo(node);
                    if (currentDistance < distance) {
                        closest = segment;
                        distance = currentDistance;
                        segmentNr = i;
                    }
                }


                var body = check.second.get(SoftBodyComponent.class);
                final var fn = body.nodes.get(segmentNr);
                final var fn2 = body.nodes.get(segmentNr + 1);
                final var intruderNr = z;
                var collision = new PointInPolygonCollision(node, closest,
                        p -> {
                            Entity entity = check.first.get(SoftBodyComponent.class).nodes.get(intruderNr);
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
                check.updateFirstPolygon();
                check.updateSecondPolygon();
                collision.moveSegment.accept(delta.multiply(-0.5));
            }
        }
    }

    private static Polygon toPolygon(Entity entity) {
        List<Vector> list = new ArrayList<>();
        for (final var n : entity.get(SoftBodyComponent.class).nodes) {
            list.add(n.position());
        }
        return Polygon.ofNodes(list);
    }

    private static List<Check> initializeChecks(List<Entity> bodies) {
        final List<Check> result = new ArrayList<>();
        for (int i = 0; i < bodies.size() - 1; i++) {
            for (int j = i + 1; j < bodies.size(); j++) {

                Entity first = bodies.get(i);
                Entity second = bodies.get(j);
                final Check check = new Check(first, second);
                if (check.isCandidate()) {
                    result.add(check);
                }
            }
        }
        return result;
    }
}
