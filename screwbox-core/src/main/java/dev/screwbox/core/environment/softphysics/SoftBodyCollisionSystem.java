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
import static java.util.Objects.nonNull;

@ExecutionOrder(SIMULATION_PREPARE)
public class SoftBodyCollisionSystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftLinkComponent.class, SoftBodyCollisionComponent.class);
    private static final int POINT_IN_POLYGON_RESOLVE_SPEED = 10;

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
            this.firstPolygon = SoftPhysicsSupport.toPolygon(firstSoftBody);
        }

        void updateSecondPolygon() {
            this.secondPolygon = SoftPhysicsSupport.toPolygon(secondSoftBody);
        }

        public CollisionCheck inverse() {
            return new CollisionCheck(second, first);
        }

    }

    record PointInPolygonCollision(Vector intruder, Line segment, Consumer<Vector> moveIntruder,
                                   Consumer<Vector> moveSegment) {
    }

    @Override
    public void update(final Engine engine) {
        final double resolveSpeed = engine.loop().delta(POINT_IN_POLYGON_RESOLVE_SPEED);
        for (final var collisionCheck : calculateCollisionChecks(engine)) {
            final var inverseCheck = collisionCheck.inverse();
            resolveBisectorIntrusion(resolveSpeed, collisionCheck);
            resolveBisectorIntrusion(resolveSpeed, inverseCheck);
            resolvePointInPolygonCollisions(resolveSpeed, collisionCheck);
            resolvePointInPolygonCollisions(resolveSpeed, inverseCheck);
        }
    }

    private void resolveBisectorIntrusion(final double resolveSpeed, final CollisionCheck check) {
        for (int nodeNr = 0; nodeNr < check.firstPolygon.nodeCount(); nodeNr++) {
            resolveBisectorIntrusionOf(resolveSpeed, check, nodeNr);
        }
    }

    private static void resolveBisectorIntrusionOf(final double resolveSpeed, final CollisionCheck check, final int nodeNr) {
        check.firstPolygon.bisectorRay(nodeNr).ifPresent(ray -> {
            ray = Line.between(ray.start(), Vector.$((ray.end().x() + ray.start().x()) / 2.0, (ray.end().y() + ray.start().y()) / 2.0));
            for (var segment : check.secondPolygon.segments()) {
                final var intersection = ray.intersectionPoint(segment);
                if (nonNull(intersection)) {
                    final Entity entity = check.firstSoftBody.nodes.get(nodeNr);
                    entity.moveTo(intersection);
                    final var physicsComponent = entity.get(PhysicsComponent.class);
                    physicsComponent.velocity = physicsComponent.velocity.add(physicsComponent.velocity.invert().multiply(resolveSpeed)).reduce(resolveSpeed);
                    check.updateFirstPolygon();
                }
            }
        });
    }

    private static void resolvePointInPolygonCollisions(final double resolveSpeed, final CollisionCheck check) {
        for (int nodeNr = 0; nodeNr < check.firstPolygon.definitionNotes().size(); nodeNr++) {
            final var node = check.firstPolygon.definitionNotes().get(nodeNr);
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

                final var firstNode = check.secondSoftBody.nodes.get(segmentNr);
                final var secondNode = check.secondSoftBody.nodes.get(segmentNr + 1);
                final var intruderNr = nodeNr;
                final var collision = new PointInPolygonCollision(node, closest, intrusionDistance -> {
                    Entity entity = check.firstSoftBody.nodes.get(intruderNr);
                    entity.moveBy(intrusionDistance);
                    final var physicsComponent = entity.get(PhysicsComponent.class);
                    physicsComponent.velocity = physicsComponent.velocity.add(intrusionDistance.multiply(resolveSpeed));
                }, intrusionDistance -> {
                    firstNode.moveBy(intrusionDistance);
                    final var physicsComponent = firstNode.get(PhysicsComponent.class);
                    physicsComponent.velocity = physicsComponent.velocity.add(intrusionDistance.multiply(resolveSpeed));
                    secondNode.moveBy(intrusionDistance);
                    final var secondPhysicsComponent = secondNode.get(PhysicsComponent.class);
                    secondPhysicsComponent.velocity = secondPhysicsComponent.velocity.add(intrusionDistance.multiply(resolveSpeed));
                });
                final Vector closestPointToIntruder = closest.closestPoint(collision.intruder);
                final Vector delta = closestPointToIntruder.substract(collision.intruder);
                collision.moveIntruder.accept(delta.multiply(.5));
                check.updateFirstPolygon();
                check.updateSecondPolygon();
                collision.moveSegment.accept(delta.multiply(-.5));
            }
        }
    }

    private static List<CollisionCheck> calculateCollisionChecks(final Engine engine) {
        final var bodies = engine.environment().fetchAll(BODIES);
        final var checks = new ArrayList<CollisionCheck>();
        for (int i = 0; i < bodies.size() - 1; i++) {
            for (int j = i + 1; j < bodies.size(); j++) {
                final Entity first = bodies.get(i);
                final Entity second = bodies.get(j);
                final CollisionCheck check = new CollisionCheck(first, second);
                if (Bounds.around(check.firstPolygon.nodes()).intersects(Bounds.around(check.secondPolygon.nodes()))) {
                    checks.add(check);
                }
            }
        }
        return checks;
    }


}
