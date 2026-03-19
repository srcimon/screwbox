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

import static dev.screwbox.core.environment.Order.SIMULATION_PREPARE;
import static java.util.Objects.nonNull;

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
            // Bounds direkt vom aktuellen Shape nehmen (präziser)
            final Bounds bodyBounds = Bounds.around(softBody.shape.nodes());

            for (final var collider : colliders) {
                if (!bodyBounds.intersects(collider.bounds())) {
                    continue;
                }

                final Polygon colliderPoly = Polygon.fromBounds(collider.bounds());
                final var colliderSegments = colliderPoly.segments();
                final var bodySegments = softBody.shape.segments();


                // 2. EDGE-INTERSECTION CHECK (Gegen das Durchfallen)
                for (int i = 0; i < bodySegments.size(); i++) {
                    final Line bodyEdge = bodySegments.get(i);

                    for (final Line landscapeEdge : colliderSegments) {
                        final Vector intersection = bodyEdge.intersectionPoint(landscapeEdge);

                        if (nonNull(intersection)) {
                            // Normale berechnen (wie bisher)
                            Vector direction = landscapeEdge.start().substract(landscapeEdge.end());
                            Vector normal = direction.invert().normalize();
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

                // 3. COLLIDER-IN-SOFTBODY CHECK (Wenn der Collider kleiner als der Softbody ist)
                final Polygon softBodyPoly = softBody.shape;

// Wir prüfen die 4 Ecken der Collider-Bounds
                for (final Vector corner : Polygon.fromBounds(collider.bounds()).nodes()) {
                    if (softBodyPoly.contains(corner)) {
                        // Der Collider-Punkt ist im Softbody.
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
                            Vector edgeDir = closestEdge.end().substract(closestEdge.start());
                            Vector normal = Vector.of(-edgeDir.y(), edgeDir.x()).normalize();

                            // Sicherstellen, dass die Normale vom Softbody-Zentrum wegzeigt
                            if (dotProduct(normal, closestEdge.center().substract(softBodyPoly.center())) < 0) {
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
                }
            }
            softBody.shape = SoftPhysicsSupport.toPolygon(softBody.nodes);
        }
    }

    private void applyWeightedImpulseResponse(Entity node, Vector normal, double weight) {
        final var physics = node.get(PhysicsComponent.class);
        if (nonNull(physics)) {
            double dot = dotProduct(physics.velocity, normal);

            if (dot < 0) {
                // Nur den Anteil der Geschwindigkeit korrigieren, der dem Gewicht entspricht
                physics.velocity = physics.velocity.substract(normal.multiply(dot * 1.05 * weight));
            }
            // Reibung leicht skalieren
            physics.velocity = physics.velocity.multiply(1.0 - (0.2 * weight));
        }
    }

    public double dotProduct(Vector a, Vector b) {
        return (a.x() * b.x()) + (a.y() * b.y());
    }

}
