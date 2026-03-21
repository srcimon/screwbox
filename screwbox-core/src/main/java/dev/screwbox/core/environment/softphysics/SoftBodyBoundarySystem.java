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

    private static final double IMPULSE_RESPONSE = 1.05;

    @Override
    public void update(final Engine engine) {
        final var bodies = engine.environment().fetchAll(BODIES);
        final var colliders = engine.environment().fetchAll(COLLIDERS);

        for (final var body : bodies) {
            final var softBody = body.get(SoftBodyComponent.class);
            final Bounds bodyBounds = Bounds.around(softBody.shape.nodes());

            for (final var collider : colliders) {
                if (bodyBounds.intersects(collider.bounds())) {
                    runEdgeIntersectionCheck(collider, softBody);
                    runColliderInSoftBodyCheck(collider, softBody);
                    softBody.shape = SoftPhysicsSupport.toPolygon(softBody.nodes);
                }
            }
        }
    }

    private void runEdgeIntersectionCheck(final Entity collider, final SoftBodyComponent softBody) {
        final List<Line> colliderSegments = collider.bounds().borders();
        final List<Line> bodySegments = softBody.shape.segments();
        for (int i = 0; i < bodySegments.size(); i++) {
            final Line bodyEdge = bodySegments.get(i);

            for (final Line landscapeEdge : colliderSegments) {
                final Vector intersection = bodyEdge.intersectionPoint(landscapeEdge);

                if (nonNull(intersection)) {
                    Vector normal = landscapeEdge.end().substract(landscapeEdge.start()).normalize();
                    if (dotProduct(intersection.substract(collider.position()), normal) < 0) {
                        normal = normal.invert();
                    }

                    final Entity nodeA = softBody.nodes.get(i);
                    final Entity nodeB = softBody.nodes.get((i + 1) % softBody.nodes.size());

                    double distA = nodeA.position().distanceTo(intersection);

                    double weightA = Math.clamp(1.0 - (distA / bodyEdge.length()), 0, 1);
                    double weightB = 1.0 - weightA; // Der Rest geht an Node B
                    Vector basePush = normal.multiply(0.5);

                    nodeA.moveBy(basePush.multiply(weightA));
                    nodeB.moveBy(basePush.multiply(weightB));
                    applyWeightedImpulseResponse(nodeA, normal, weightA);
                    applyWeightedImpulseResponse(nodeB, normal, weightB);
                }
            }
        }
    }

    private void runColliderInSoftBodyCheck(final Entity collider, final SoftBodyComponent softBody) {
        for (final Vector corner : collider.bounds().corners()) {
            if (softBody.shape.contains(corner)) {
                pushBackNearestSegmentOfSoftBody(softBody, corner);
            }
        }
    }
    double PUSH_MAGNITUDE = 0.01;
    private void pushBackNearestSegmentOfSoftBody(final SoftBodyComponent softBody, final Vector corner) {
        final List<Line> bodySegments = softBody.shape.segments();
        Line closestEdge = null;
        double minDistance = Double.MAX_VALUE;
        int edgeIndex = -1;

        for (int i = 0; i < bodySegments.size(); i++) {
            double dist = bodySegments.get(i).center().distanceTo(corner);
            if (dist < minDistance) {
                minDistance = dist;
                closestEdge = bodySegments.get(i);
                edgeIndex = i;
            }
        }

        if (nonNull(closestEdge)) {
            Vector normal = closestEdge.start().substract(closestEdge.end()).normalize();

            if (dotProduct(normal, closestEdge.center().substract(softBody.shape.center())) < 0) {
                normal = normal.invert();
            }

            final Vector push = normal.multiply((1.0 - minDistance) * PUSH_MAGNITUDE);

            final Entity nodeA = softBody.nodes.get(edgeIndex);
            final Entity nodeB = softBody.nodes.get((edgeIndex + 1) % softBody.nodes.size());

            nodeA.moveBy(push);
            nodeB.moveBy(push);

            applyWeightedImpulseResponse(nodeA, normal, 1.0);
            applyWeightedImpulseResponse(nodeB, normal, 1.0);
        }
    }

    private void applyWeightedImpulseResponse(final Entity node, final Vector normal, final double weight) {
        final var physics = node.get(PhysicsComponent.class);
        if (nonNull(physics)) {
            double dot = dotProduct(physics.velocity, normal);
            if (dot < 0) {
                Vector delta = normal.multiply(dot * IMPULSE_RESPONSE * weight);
                physics.velocity = physics.velocity.substract(delta);
            }
        }
    }

    public double dotProduct(final Vector first, final Vector second) {
        return first.x() * second.x() + first.y() * second.y();
    }
}