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
                if (!bodyBounds.intersects(collider.bounds())) {
                    continue;
                }

                final var colliderSegments = Polygon.fromBounds(collider.bounds()).segments();
                final var bodySegments = softBody.shape.segments();

                for (int i = 0; i < bodySegments.size(); i++) {
                    final Line bodyEdge = bodySegments.get(i);

                    for (final Line landscapeEdge : colliderSegments) {
                        final Vector intersection = bodyEdge.intersectionPoint(landscapeEdge);

                        if (nonNull(intersection)) {
                            // 1. Normale berechnen
                            Vector edgeDir = landscapeEdge.end().substract(landscapeEdge.start());
                            Vector normal = Vector.of(-edgeDir.y(), edgeDir.x()).normalize();

                            // Korrekte Ausrichtung der Normale (nach außen vom Collider)
                            if (dotProduct(intersection.substract(collider.bounds().position()), normal) < 0) {
                                normal = normal.invert();
                            }

                            // 2. Gewichtung berechnen (Wo auf der Kante passierte der Einschlag?)
                            double distA = intersection.distanceTo(bodyEdge.start());
                            double distTotal = bodyEdge.length();
                            double weightB = distA / distTotal; // 0.0 (bei A) bis 1.0 (bei B)
                            double weightA = 1.0 - weightB;

                            final Entity nodeA = softBody.nodes.get(i);
                            final Entity nodeB = softBody.nodes.get((i + 1) % softBody.nodes.size());

                            // 3. Punktgenauer Push (verhindert das starre Verschieben)
                            Vector push = normal.multiply(0.5);
                            nodeA.moveBy(push.multiply(weightA));
                            nodeB.moveBy(push.multiply(weightB));

                            applyImpulseResponse(nodeA, normal, weightA);
                            applyImpulseResponse(nodeB, normal, weightB);
                        }
                    }
                }
            }
            softBody.shape = toPolygon(softBody);
        }
    }

    private static Polygon toPolygon(final SoftBodyComponent softBody) {
        final List<Vector> nodes = new ArrayList<>();
        for (final var node : softBody.nodes) {
            nodes.add(node.position());
        }
        return Polygon.ofNodes(nodes);
    }

    private void applyImpulseResponse(Entity node, Vector normal, double weight) {
        final var physics = node.get(PhysicsComponent.class);
        if (nonNull(physics)) {
            double dot = dotProduct(physics.velocity, normal);

            if (dot < 0) {
                // Nur die Komponente RICHTUNG Wand neutralisieren
                // 1.05 für leichten Bounce
                physics.velocity = physics.velocity.substract(normal.multiply(dot * 1.05 * weight));
            }

            // REIBUNG REDUZIEREN:
            // 0.8 in jedem Update tötet die Gravitation.
            // Nutze einen Wert näher an 1.0 oder wende ihn nur an, wenn ein Kontakt besteht.
            physics.velocity = physics.velocity.multiply(0.98);
        }
    }

    public double dotProduct(Vector a, Vector b) {
        return (a.x() * b.x()) + (a.y() * b.y());
    }

}
