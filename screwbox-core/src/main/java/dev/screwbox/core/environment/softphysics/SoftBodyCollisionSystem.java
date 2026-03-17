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
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.environment.Order.SIMULATION_PREPARE;
import static java.util.Objects.nonNull;

@ExecutionOrder(SIMULATION_PREPARE)
public class SoftBodyCollisionSystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftLinkComponent.class, SoftBodyCollisionComponent.class);

    private static final int POINT_IN_POLYGON_RESOLVE_SPEED = 10;
    private static final double ON_COLLISION_DAMPING = 0.8;
    private static final double RESPONSE_FACTOR = 0.8;

    private record CollisionCheck(Entity first,
                                  Entity second,
                                  SoftBodyComponent firstSoftBody,
                                  SoftBodyComponent secondSoftBody,
                                  SoftBodyCollisionComponent firstCollision,
                                  SoftBodyCollisionComponent secondCollision) {

        public CollisionCheck(final Entity first, SoftBodyComponent firstSoftBody, final Entity second) {
            this(first, second, firstSoftBody, second.get(SoftBodyComponent.class), first.get(SoftBodyCollisionComponent.class), second.get(SoftBodyCollisionComponent.class));
        }

        public CollisionCheck inverse() {
            return new CollisionCheck(second, first, secondSoftBody, firstSoftBody, secondCollision, firstCollision);
        }

    }

    @Override
    public void update(final Engine engine) {
        final double resolveSpeed = engine.loop().delta(POINT_IN_POLYGON_RESOLVE_SPEED);
        final var bodies = engine.environment().fetchAll(BODIES);

        for (final var body : bodies) {
            final var collisionComponent = body.get(SoftBodyCollisionComponent.class);
            collisionComponent.collidedNodes.clear();
            collisionComponent.collidedSegments.clear();
        }

        for (final var collisionCheck : calculateCollisionChecks(bodies)) {
            final var inverseCheck = collisionCheck.inverse();
            resolvePointInPolygonCollisions(resolveSpeed, collisionCheck);
            resolvePointInPolygonCollisions(resolveSpeed, inverseCheck);
            resolveBisectorIntrusion(resolveSpeed, collisionCheck);
            resolveBisectorIntrusion(resolveSpeed, inverseCheck);
        }

        for (final var collider : engine.environment().fetchAll(Archetype.ofSpacial(ColliderComponent.class))) {
            final Polygon colliderPolygon = Polygon.fromBounds(collider.bounds());

            for (final var body : bodies) {
                final var softBody = body.get(SoftBodyComponent.class);

                // Performance-Check: Nur weitermachen, wenn die Bounds sich überhaupt berühren
                if (!Bounds.around(softBody.shape.nodes()).intersects(collider.bounds())) {
                    continue;
                }

                // Wir prüfen jede Ecke des Colliders: Steckt sie im Softbody?
                for (final var corner : colliderPolygon.nodes()) {
                    if (softBody.shape.contains(corner)) {

                        // Finde das nächstgelegene Segment des Softbodys zu dieser Ecke
                        int closestSegmentIdx = 0;
                        double minDistance = Double.MAX_VALUE;

                        final var segments = softBody.shape.segments();
                        for (int i = 0; i < segments.size(); i++) {
                            double dist = segments.get(i).center().distanceTo(corner);
                            if (dist < minDistance) {
                                minDistance = dist;
                                closestSegmentIdx = i;
                            }
                        }

                        // Berechne den Vektor, um das Segment von der Ecke wegzudrücken
                        final Vector closestPointOnSegment = segments.get(closestSegmentIdx).closestPoint(corner);
                        final Vector pushOut = closestPointOnSegment.substract(corner).invert().multiply(RESPONSE_FACTOR);

                        // Verteile die Kraft auf die beiden Nodes, die das Segment bilden
                        final Entity nodeA = softBody.nodes.get(closestSegmentIdx);
                        final Entity nodeB = softBody.nodes.get((closestSegmentIdx + 1) % softBody.nodes.size());

                        nodeA.moveBy(pushOut);
                        nodeB.moveBy(pushOut);

                        // Dämpfung, um das "Zittern" oder Explodieren zu verhindern
                        applyDamping(nodeA, pushOut, resolveSpeed);
                        applyDamping(nodeB, pushOut, resolveSpeed);
                    }
                }
                softBody.shape = toPolygon(softBody);
            }
        }
    }

    private void applyDamping(Entity node, Vector pushOut, double resolveSpeed) {
        final var physics = node.get(PhysicsComponent.class);
        if (nonNull(physics)) {
            // Normalisiere den Korrektur-Vektor (Richtung aus dem Collider heraus)
            final Vector normal = pushOut.normalize();

            // Wie viel der aktuellen Geschwindigkeit geht "gegen" den Collider?
            double dot = dotProduct(physics.velocity, normal);

            if (dot < 0) {
                // Nur die Komponente der Geschwindigkeit dämpfen, die in den Collider hineinzeigt
                final Vector bounceComponent = normal.multiply(dot);
                physics.velocity = physics.velocity.substract(bounceComponent.multiply(1.0 + ON_COLLISION_DAMPING));
            }

            // Minimale Reibung/Dämpfung für Stabilität, aber kein "Stop"
            physics.velocity = physics.velocity.reduce(resolveSpeed * 0.1);
        }
    }
    public double dotProduct(Vector a, Vector b) {
        return (a.x() * b.x()) + (a.y() * b.y());
    }

    private static void resolveBisectorIntrusion(final double resolveSpeed, final CollisionCheck check) {
        for (int nodeNr = 0; nodeNr < check.firstSoftBody.shape.nodeCount(); nodeNr++) {
            resolveBisectorIntrusionOf(resolveSpeed, check, nodeNr);
        }
    }

    private static void resolveBisectorIntrusionOf(final double resolveSpeed, final CollisionCheck check, final int nodeNr) {
        check.firstSoftBody.shape.bisectorRay(nodeNr).ifPresent(ray -> {
            final var shortRay = Line.between(ray.start(), ray.center());
            int segmentNr = 0;
            for (final var segment : check.secondSoftBody.shape.segments()) {
                final var intersection = shortRay.intersectionPoint(segment);
                if (nonNull(intersection)) {
                    final Entity entity = check.firstSoftBody.nodes.get(nodeNr);
                    check.secondCollision.collidedSegments.add(segmentNr);
                    entity.moveBy(intersection.substract(entity.position()).multiply(RESPONSE_FACTOR));
                    final var physicsComponent = entity.get(PhysicsComponent.class);
                    if (nonNull(physicsComponent)) {
                        physicsComponent.velocity = physicsComponent.velocity.multiply(ON_COLLISION_DAMPING).reduce(resolveSpeed);
                    }
                    check.firstSoftBody.shape = toPolygon(check.firstSoftBody);
                }
                segmentNr++;
            }
        });
    }

    private static void resolvePointInPolygonCollisions(final double resolveSpeed, final CollisionCheck check) {
        for (int nodeNr = 0; nodeNr < check.firstSoftBody.shape.definitionNotes().size(); nodeNr++) {
            final var node = check.firstSoftBody.shape.definitionNotes().get(nodeNr);
            if (check.secondSoftBody.shape.contains(node)) {
                resolvePointInPolygonCollision(resolveSpeed, check, node, nodeNr);
            }
        }
    }

    private static void resolvePointInPolygonCollision(final double resolveSpeed, final CollisionCheck check, final Vector node, final int nodeNr) {
        Line closest = check.secondSoftBody.shape.segments().getFirst();
        double distance = closest.closestPoint(node).distanceTo(node);
        int segmentNr = 0;
        for (int i = 1; i < check.secondSoftBody.shape.segments().size(); i++) {
            var segment = check.secondSoftBody.shape.segments().get(i);
            double currentDistance = segment.closestPoint(node).distanceTo(node);
            if (currentDistance < distance) {
                closest = segment;
                distance = currentDistance;
                segmentNr = i;
            }
        }

        final Vector intrusionMotion = closest.closestPoint(node).substract(node).multiply(0.5);
        final Entity intruder = check.firstSoftBody.nodes.get(nodeNr);
        check.firstCollision.collidedNodes.add(nodeNr);
        intruder.moveBy(intrusionMotion);
        final var intruderPhysics = intruder.get(PhysicsComponent.class);
        if (nonNull(intruderPhysics)) {
            intruderPhysics.velocity = intruderPhysics.velocity.add(intrusionMotion.multiply(resolveSpeed)).multiply(ON_COLLISION_DAMPING);
        }

        final Vector antiIntrusionMotion = intrusionMotion.invert();

        final var firstNode = check.secondSoftBody.nodes.get(segmentNr);
        firstNode.moveBy(antiIntrusionMotion);
        final var firstPhysics = firstNode.get(PhysicsComponent.class);
        if (nonNull(firstPhysics)) {
            firstPhysics.velocity = firstPhysics.velocity.add(antiIntrusionMotion.multiply(resolveSpeed));
        }

        final var secondNode = check.secondSoftBody.nodes.get(segmentNr + 1);
        secondNode.moveBy(antiIntrusionMotion);
        final var secondPhysics = secondNode.get(PhysicsComponent.class);
        if (nonNull(secondPhysics)) {
            secondPhysics.velocity = secondPhysics.velocity.add(antiIntrusionMotion.multiply(resolveSpeed));
        }
        check.firstSoftBody.shape = toPolygon(check.firstSoftBody);
        check.secondSoftBody.shape = toPolygon(check.secondSoftBody);
    }

    private static List<CollisionCheck> calculateCollisionChecks(final List<Entity> bodies) {
        final var checks = new ArrayList<CollisionCheck>();
        for (int i = 0; i < bodies.size() - 1; i++) {
            final Entity first = bodies.get(i);
            final var firstSoftBody = first.get(SoftBodyComponent.class);
            final Bounds firstBounds = Bounds.around(firstSoftBody.shape.nodes());
            for (int j = i + 1; j < bodies.size(); j++) {
                final Entity second = bodies.get(j);

                final CollisionCheck check = new CollisionCheck(first, firstSoftBody, second);

                if (firstBounds.intersects(Bounds.around(check.secondSoftBody.shape.nodes()))) {
                    checks.add(check);
                }
            }
        }
        return checks;
    }

    private static Polygon toPolygon(final SoftBodyComponent softBody) {
        final List<Vector> nodes = new ArrayList<>();
        for (final var node : softBody.nodes) {
            nodes.add(node.position());
        }
        return Polygon.ofNodes(nodes);
    }
}
