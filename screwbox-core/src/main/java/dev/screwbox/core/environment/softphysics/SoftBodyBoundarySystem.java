package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.navigation.Borders;

import java.util.List;

import static dev.screwbox.core.environment.Order.SIMULATION_PREPARE;
import static java.util.Objects.nonNull;

/**
 * Processes all soft bodies with a {@link SoftBodyBoundaryComponent} and adds collisions with colliders to the soft
 * bodies boundary.
 *
 * @since 3.26.0
 */
@ExecutionOrder(SIMULATION_PREPARE)
public class SoftBodyBoundarySystem implements EntitySystem {

    private static final Archetype BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftBodyBoundaryComponent.class);
    private static final Archetype COLLIDERS = Archetype.ofSpacial(ColliderComponent.class);

    @Override
    public void update(final Engine engine) {
        final var bodies = engine.environment().fetchAll(BODIES);
        final var colliders = engine.environment().fetchAll(COLLIDERS);

        for (final var body : bodies) {
            final var softBody = body.get(SoftBodyComponent.class);
            final Bounds bodyBounds = Bounds.around(softBody.shape.nodes());

            for (final var collider : colliders) {
                if (bodyBounds.intersects(collider.bounds())) {
                    reactOnCollision(collider, softBody);
                    softBody.shape = SoftPhysicsSupport.toPolygon(softBody.nodes);
                }
            }
        }
    }

    private void reactOnCollision(final Entity collider, final SoftBodyComponent softBody) {
        final var colliderSegments = Borders.ALL.extractFrom(collider.bounds());
        final var softBodySegments = softBody.shape.segments();

        runEdgeIntersectionCheck(collider, softBody, softBodySegments, colliderSegments);
        runColliderInSoftBodyCheck(collider, softBody, softBodySegments);
    }

    private void runEdgeIntersectionCheck(Entity collider, SoftBodyComponent softBody, List<Line> bodySegments, List<Line> colliderSegments) {
        for (int i = 0; i < bodySegments.size(); i++) {
            final Line bodyEdge = bodySegments.get(i);

            for (final Line landscapeEdge : colliderSegments) {
                final Vector intersection = bodyEdge.intersectionPoint(landscapeEdge);

                if (nonNull(intersection)) {
                    // Normale berechnen (wie bisher)
                    Vector normal = landscapeEdge.end().substract(landscapeEdge.start()).normalize();
                    if (dotProduct(intersection.substract(collider.bounds().position()), normal) < 0) {
                        normal = normal.invert();
                    }

                    final Entity nodeA = softBody.nodes.get(i);
                    final Entity nodeB = softBody.nodes.get((i + 1) % softBody.nodes.size());

                    // GEWICHTUNG BERECHNEN
                    double edgeLength = bodyEdge.length();
                    // Distanz von Node A zum Schnittpunkt (normalisiert auf 0.0 bis 1.0)
                    double distA = nodeA.position().distanceTo(intersection);

                    // Gewichtung: Wenn der Schnittpunkt direkt bei A ist, bekommt A 100% (1.0)
                    // Wir nutzen (1 - relativeDistanz), um den nahen Knoten stärker zu pushen
                    double weightA = Math.clamp(1.0 - (distA / edgeLength), 0, 1);
                    double weightB = 1.0 - weightA; // Der Rest geht an Node B

                    // Push-Stärke (0.5 als Basis-Offset)
                    Vector basePush = normal.multiply(0.5);

                    // Bewegung gewichtet anwenden
                    nodeA.moveBy(basePush.multiply(weightA));
                    nodeB.moveBy(basePush.multiply(weightB));

                    // Impuls-Antwort ebenfalls gewichten, damit der "Stop" präziser wirkt
                    applyWeightedImpulseResponse(nodeA, normal, weightA);
                    applyWeightedImpulseResponse(nodeB, normal, weightB);
                }
            }
        }
    }

    private void runColliderInSoftBodyCheck(Entity collider, SoftBodyComponent softBody, List<Line> bodySegments) {
        for (final Vector corner : collider.bounds().corners()) {
            if (softBody.shape.contains(corner)) {
                pushBackNearestSegmentOfSoftBody(softBody, bodySegments, corner);
            }
        }
    }

    private void pushBackNearestSegmentOfSoftBody(SoftBodyComponent softBody, List<Line> bodySegments, Vector corner) {
        // Wir müssen die naheliegendste Kante des Softbodys finden und diese wegdrücken.

        Line closestEdge = null;
        double minDistance = Double.MAX_VALUE;
        int edgeIndex = -1;

        for (int j = 0; j < bodySegments.size(); j++) {
            double dist = bodySegments.get(j).center().distanceTo(corner);
            if (dist < minDistance) {
                minDistance = dist;
                closestEdge = bodySegments.get(j);
                edgeIndex = j;
            }
        }

        if (nonNull(closestEdge)) {
            // Berechne die Normale der Softbody-Kante (nach außen gerichtet)
            Vector normal = closestEdge.start().substract(closestEdge.end()).normalize();

            // Sicherstellen, dass die Normale vom Softbody-Zentrum wegzeigt
            if (dotProduct(normal, closestEdge.center().substract(softBody.shape.center())) < 0) {
                normal = normal.invert();
            }

            // Drücke die beteiligten Nodes des Softbodys weg
            double pushMag = (1.0 - minDistance) * 0.01; // Stärke des Schubs
            Vector push = normal.multiply(pushMag);

            final Entity nodeA = softBody.nodes.get(edgeIndex);
            final Entity nodeB = softBody.nodes.get((edgeIndex + 1) % softBody.nodes.size());

            nodeA.moveBy(push);
            nodeB.moveBy(push);

            applyWeightedImpulseResponse(nodeA, normal, 1.0);
            applyWeightedImpulseResponse(nodeB, normal, 1.0);
        }
    }

    private void applyWeightedImpulseResponse(Entity node, Vector normal, double weight) {
        final var physics = node.get(PhysicsComponent.class);
        if (nonNull(physics)) {
            double dot = dotProduct(physics.velocity, normal);
            if (dot < 0) {
                // Nur den Anteil der Geschwindigkeit korrigieren, der dem Gewicht entspricht
                Vector delta = normal.multiply(dot * 1.05 * weight);
                physics.velocity = physics.velocity.substract(delta);
            }
        }
    }

    public double dotProduct(Vector a, Vector b) {
        return a.x() * b.x() + a.y() * b.y();
    }

}
