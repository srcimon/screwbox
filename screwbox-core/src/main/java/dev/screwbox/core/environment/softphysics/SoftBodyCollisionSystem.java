package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.Polygon;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static dev.screwbox.core.environment.Order.SIMULATION_PREPARE;

@ExecutionOrder(SIMULATION_PREPARE)
public class SoftBodyCollisionSystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftLinkComponent.class, SoftBodyCollisionComponent.class);

    private static final class CollisionCheck {

        private Polygon firstPolygon;
        private Polygon secondPolygon;
        private final Entity first;
        private final Entity second;
        private final SoftBodyComponent firstSoftBody;
        private final SoftBodyComponent secondSoftBody;

        public CollisionCheck(final Entity first, final Entity second) {
            this.first = first;
            this.second = second;
            this.firstSoftBody = first.get(SoftBodyComponent.class);
            this.secondSoftBody = second.get(SoftBodyComponent.class);
            updateFirstPolygon();
            updateSecondPolygon();
        }

        void updateFirstPolygon() {
            this.firstPolygon = toPolygon(firstSoftBody);
        }

        void updateSecondPolygon() {
            this.secondPolygon = toPolygon(secondSoftBody);
        }

        public CollisionCheck inverse() {
            return new CollisionCheck(second, first);
        }

        private static Polygon toPolygon(final SoftBodyComponent component) {
            final List<Vector> list = new ArrayList<>();
            for (final var node : component.nodes) {
                list.add(node.position());
            }
            return Polygon.ofNodes(list);
        }
    }

    record PointInPolygonCollision(Vector intruder, Line segment, Consumer<Vector> moveIntruder,
                                   Consumer<Vector> moveSegment) {
    }

    @Override
    public void update(final Engine engine) {
        for (final var collisionCheck : calculateCollisionChecks(engine)) {
            final var inverseCheck = collisionCheck.inverse();
            resolveBisectorIntrusion(collisionCheck);
            resolveBisectorIntrusion(inverseCheck);
            resolvePointInPolygonCollisions(engine, collisionCheck);
            resolvePointInPolygonCollisions(engine, inverseCheck);
        }
    }

    private void resolveBisectorIntrusion(CollisionCheck check) {
        for (int i = 0; i < check.firstPolygon.nodeCount(); i++) {

            var rayo = check.firstPolygon.bisectorRay(i);
            if (rayo.isPresent()) {
                var ray = rayo.get();
                ray = Line.between(ray.start(), Vector.$((ray.end().x() + ray.start().x()) / 2.0, (ray.end().y() + ray.start().y()) / 2.0));
                for (var segment : check.secondPolygon.segments()) {
                    final var intersection = ray.intersectionPoint(segment);
                    if (intersection != null) {
                        Entity entity = check.firstSoftBody.nodes.get(i);
                        entity.moveTo(intersection);
                        entity.get(PhysicsComponent.class).velocity = Vector.zero();
                        check.updateFirstPolygon();
                    }

                }
            }
        }
    }


    private static void resolvePointInPolygonCollisions(Engine engine, CollisionCheck check) {
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


                final var fn = check.secondSoftBody.nodes.get(segmentNr);
                final var fn2 = check.secondSoftBody.nodes.get(segmentNr + 1);
                final var intruderNr = z;
                var collision = new PointInPolygonCollision(node, closest,
                        p -> {
                            Entity entity = check.firstSoftBody.nodes.get(intruderNr);
                            entity.moveBy(p);
                            PhysicsComponent physicsComponent = entity.get(PhysicsComponent.class);
                            physicsComponent.velocity = physicsComponent.velocity.add(p.multiply(10 * engine.loop().delta()));
                        },
                        p -> {
                            fn.moveBy(p);
                            PhysicsComponent physicsComponent = fn.get(PhysicsComponent.class);
                            physicsComponent.velocity = physicsComponent.velocity.add(p.multiply(10 * engine.loop().delta()));
                            fn2.moveBy(p);
                            PhysicsComponent physicsComponent1 = fn2.get(PhysicsComponent.class);
                            physicsComponent1.velocity = physicsComponent1.velocity.add(p.multiply(10 * engine.loop().delta()));
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

    private static List<CollisionCheck> calculateCollisionChecks(final Engine engine) {
        final var bodies = engine.environment().fetchAll(BODIES);
        final var checks = new ArrayList<CollisionCheck>();
        for (int i = 0; i < bodies.size() - 1; i++) {
            for (int j = i + 1; j < bodies.size(); j++) {
                Entity first = bodies.get(i);
                Entity second = bodies.get(j);
                final CollisionCheck check = new CollisionCheck(first, second);
                if (Bounds.around(check.firstPolygon.nodes()).intersects(Bounds.around(check.secondPolygon.nodes()))) {
                    checks.add(check);
                }
            }
        }
        return checks;
    }


}
