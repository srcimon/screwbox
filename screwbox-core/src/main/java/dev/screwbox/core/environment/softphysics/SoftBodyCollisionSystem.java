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

        for (final var body : bodies) {
            final var softBody = body.get(SoftBodyComponent.class);
            final Bounds bodyBounds = Bounds.around(softBody.shape.nodes());

            for (final var collider : engine.environment().fetchAllHaving(ColliderComponent.class)) {
                if (!bodyBounds.intersects(collider.bounds())) {
                    continue;
                }

                final Polygon colliderPoly = Polygon.fromBounds(collider.bounds());
                final var bodySegments = softBody.shape.segments();
                final var colliderSegments = colliderPoly.segments();

                // 1. NODE-IN-POLYGON CHECK (Nodes vor dem Durchfallen bewahren)
                for (int i = 0; i < softBody.nodes.size(); i++) {
                    final Entity nodeEntity = softBody.nodes.get(i);
                    final Vector pos = nodeEntity.position();

                    if (colliderPoly.contains(pos)) {
                        // Finde den exakten Austrittspunkt am nächsten Segment
                        Vector closest = findClosestPointOnPolygon(pos, colliderSegments);
                        Vector push = closest.substract(pos);

                        nodeEntity.moveBy(push);
                        applyImpulseResponse(nodeEntity, push.normalize(), resolveSpeed);
                    }
                }

                // 2. EDGE-INTERSECTION CHECK (Verhindert, dass Kanten sich schneiden)
                for (int i = 0; i < bodySegments.size(); i++) {
                    final Line bodyEdge = bodySegments.get(i);

                    for (final Line landscapeEdge : colliderSegments) {
                        final Vector intersection = bodyEdge.intersectionPoint(landscapeEdge);

                        // Innerhalb der Edge-Intersection Schleife
                        if (nonNull(intersection)) {
                            // 1. Richtungsvektor der Landschafts-Kante berechnen
                            Vector direction = landscapeEdge.start().substract(landscapeEdge.end());

                            // 2. Normale berechnen (Senkrecht zur Kante: -y, x)
                            // Wir normalisieren sie direkt für die Impuls-Antwort
                            Vector normal = Vector.of(-direction.y(), direction.x()).normalize();

                            // Sicherstellen, dass die Normale VOM Collider WEG zeigt (nach außen)
                            // Wenn das Skalarprodukt mit dem Vektor zum Softbody positiv ist, zeigt sie richtig
                            Vector toBody = intersection.substract(collider.bounds().position());
                            if (dotProduct(toBody, normal) < 0) {
                                normal = normal.invert();
                            }

                            final Vector push = normal; // Kleiner Sicherheits-Offset

                            final Entity nodeA = softBody.nodes.get(i);
                            final Entity nodeB = softBody.nodes.get((i + 1) % softBody.nodes.size());

                            nodeA.moveBy(push);
                            nodeB.moveBy(push);

                            applyImpulseResponse(nodeA, normal, resolveSpeed);
                            applyImpulseResponse(nodeB, normal, resolveSpeed);
                        }
                    }
                }
            }
            softBody.shape = toPolygon(softBody);
        }
    }

    private void applyImpulseResponse(Entity node, Vector normal, double resolveSpeed) {
        final var physics = node.get(PhysicsComponent.class);
        if (nonNull(physics)) {
            double dot = dotProduct(physics.velocity, normal);

            // Wenn der Node sich in den Collider hineinbewegt (dot < 0)
            if (dot < 0) {
                // Wir ziehen die Eindring-Geschwindigkeit exakt ab (dot ist negativ!)
                // 1.0 = exakt stoppen, 1.1 = minimaler Abprall (Bouncy)
                // Hier nutzen wir 1.0, um das "Bouncy as hell" zu stoppen.
                physics.velocity = physics.velocity.substract(normal.multiply(dot * 1.0));
            }

            // Reibung: Verlangsamt seitliches Rutschen (Gleitreibung)
            physics.velocity = physics.velocity.multiply(0.9);
        }
    }

    private Vector findClosestPointOnPolygon(Vector point, List<Line> segments) {
        Vector closest = segments.getFirst().closestPoint(point);
        double minFacingDist = closest.distanceTo(point);
        for (Line segment : segments) {
            Vector p = segment.closestPoint(point);
            double d = p.distanceTo(point);
            if (d < minFacingDist) {
                minFacingDist = d;
                closest = p;
            }
        }
        return closest;
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
