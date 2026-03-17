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

    private static final Archetype BODIES = Archetype.ofSpacial(SoftBodyComponent.class, SoftLinkComponent.class, SoftBodyBoundaryComponent.class);
    @Override
    public void update(final Engine engine) {
        final var bodies = engine.environment().fetchAll(BODIES);
        for (final var body : bodies) {
            final var softBody = body.get(SoftBodyComponent.class);
            // Bounds direkt vom aktuellen Shape nehmen (präziser)
            final Bounds bodyBounds = Bounds.around(softBody.shape.nodes());

            for (final var collider : engine.environment().fetchAllHaving(ColliderComponent.class)) {
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
                            Vector direction = landscapeEdge.start().substract(landscapeEdge.end());
                            Vector normal = Vector.of(-direction.y(), direction.x()).normalize();

                            // Ausrichtung der Normalen nach außen
                            if (dotProduct(intersection.substract(collider.bounds().position()), normal) < 0) {
                                normal = normal.invert();
                            }

                            // Nodes der Kante sanft rausstellen
                            final Entity nodeA = softBody.nodes.get(i);
                            final Entity nodeB = softBody.nodes.get((i + 1) % softBody.nodes.size());

                            // Ein kleiner Push reicht, um die mathematische Schnittmenge zu verlassen
                            Vector edgePush = normal.multiply(0.5);
                            nodeA.moveBy(edgePush);
                            nodeB.moveBy(edgePush);

                            applyImpulseResponse(nodeA, normal);
                            applyImpulseResponse(nodeB, normal);
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

    private void applyImpulseResponse(Entity node, Vector normal) {
        final var physics = node.get(PhysicsComponent.class);
        if (nonNull(physics)) {
            double dot = dotProduct(physics.velocity, normal);

            if (dot < 0) {
                // 1.0 bedeutet: Geschwindigkeit zur Wand wird exakt NULL.
                // Erhöhe auf 1.05 für minimalen Bounce, oder lass es bei 1.0 für "Dead Stop".
                physics.velocity = physics.velocity.substract(normal.multiply(dot * 1.05));
            }

            // Reibung (Friction): 0.8 stoppt das seitliche "Wobbeln" sehr effektiv
            physics.velocity = physics.velocity.multiply(0.8);
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

    public double dotProduct(Vector a, Vector b) {
        return (a.x() * b.x()) + (a.y() * b.y());
    }

}
