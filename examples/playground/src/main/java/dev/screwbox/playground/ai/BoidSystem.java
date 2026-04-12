package dev.screwbox.playground.ai;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.OvalDrawOptions;
import dev.screwbox.core.graphics.options.RectangleDrawOptions;
import dev.screwbox.core.navigation.RaycastBuilder;

import java.util.ArrayList;
import java.util.List;

public class BoidSystem implements EntitySystem {

    private static final Archetype BOIDS = Archetype.ofSpacial(PhysicsComponent.class, BoidComponent.class);
    private static final Archetype OBSTACLES = Archetype.ofSpacial(BoidObstacleComponent.class);

    @Override
    public void update(Engine engine) {
        var boids = engine.environment().fetchAll(BOIDS);
        boolean isFirst = true;
        for (var boid : boids) {
            final var config = boid.get(BoidComponent.class);
            final List<Entity> nearbyBoids = fetchNearbyBoids(boid, boids, config);
            if (isFirst) {
                engine.graphics().world().drawCircle(boid.position(), config.visionRadius, OvalDrawOptions.filled(Color.WHITE.opacity(0.1)).drawOrder(Order.DEBUG_OVERLAY.drawOrder()));
                for (final var nearbyBoid : nearbyBoids) {
                    engine.graphics().world().drawLine(boid.position(), nearbyBoid.position(), LineDrawOptions.color(Color.RED.opacity(0.5)).strokeWidth(2).drawOrder(Order.DEBUG_OVERLAY.drawOrder()));
                }
            }
            isFirst = false;
            double delta = engine.loop().delta();
            PhysicsComponent physics = boid.get(PhysicsComponent.class);
            var separationSteer = Vector.zero();
            // 1. separationSteer away from nearby boids (separation)
            if (!nearbyBoids.isEmpty()) {
                var diffSum = Vector.zero();
                for (final var nearbyBoid : nearbyBoids) {
                    double distance = boid.position().distanceTo(nearbyBoid.position());
                    // Verhindere Division durch Null und gewichte: je näher, desto stärker
                    var diff = boid.position().substract(nearbyBoid.position());
                    diffSum = diffSum.add(diff.divide(Math.max(0.1, distance * distance)));
                }
                var averageDiff = diffSum.divide(nearbyBoids.size());
                if (averageDiff.length() > 0) {
                    var desiredVelocity = averageDiff.length(config.velocity);
                    separationSteer = desiredVelocity.substract(physics.velocity);
                }
            }
            physics.velocity = physics.velocity.add(separationSteer.multiply(config.separationStrength * delta));

            // 2. Alignment (Mittelwert der Geschwindigkeiten)
            if (!nearbyBoids.isEmpty()) {
                var averageVelocity = Vector.zero();
                for (var nearbyBoid : nearbyBoids) {
                    averageVelocity = averageVelocity.add(nearbyBoid.get(PhysicsComponent.class).velocity);
                }
                var desiredAlignementVelocity = averageVelocity.divide(nearbyBoids.size()).length(config.velocity);
                var alignmentSteer = desiredAlignementVelocity.substract(physics.velocity);
                physics.velocity = physics.velocity.add(alignmentSteer.multiply(config.alignmentStrenth * delta));
            }

// 3. Cohesion (Mittelwert der Positionen -> Schwerpunkt)
            if (!nearbyBoids.isEmpty()) {
                var centerPos = Vector.zero();
                for (var nearbyBoid : nearbyBoids) {
                    centerPos = centerPos.add(nearbyBoid.position());
                }
                var averageCenter = centerPos.divide(nearbyBoids.size());
                var desiredCohesionDirection = averageCenter.substract(boid.position());
                var desiredCohesionVelocity = desiredCohesionDirection.length(config.velocity);
                var cohesionSteer = desiredCohesionVelocity.substract(physics.velocity);
                physics.velocity = physics.velocity.add(cohesionSteer.multiply(config.cohesionStrength * delta));
            }


            applyObstacleAvoidance(engine, boid, physics, delta);
            physics.velocity = physics.velocity.length(config.velocity);
        }


    }

    private static void applyObstacleAvoidance(Engine engine, Entity boid, PhysicsComponent physics, double delta) {
        var config = boid.get(BoidComponent.class);
        double maxDistance = config.obstacleVisionRadius;
        Vector velocity = physics.velocity;

        List<Vector> rayTargets = List.of(
            velocity.length(maxDistance),
            Angle.degrees(20).rotate(velocity).length(maxDistance),
            Angle.degrees(-20).rotate(velocity).length(maxDistance)
        );

        Vector totalSteer = Vector.zero();
        int hits = 0;

        for (var targetOffset : rayTargets) {
            var hit = engine.navigation().raycastFrom(boid.position())
                .checkingFor(OBSTACLES)
                .castingTo(boid.position().add(targetOffset))
                .nearestEntity();

            if (hit.isPresent() && !hit.get().bounds().expand(10).contains(boid.bounds())) {
                Bounds bounds = hit.get().bounds();

                // Berechne Fluchtpunkt (Weg von der nächsten Kante)
                double closestX = Math.max(bounds.minX(), Math.min(boid.position().x(), bounds.maxX()));
                double closestY = Math.max(bounds.minY(), Math.min(boid.position().y(), bounds.maxY()));
                Vector closestPoint = Vector.of(closestX, closestY);

                Vector avoidanceDirection = boid.position().substract(closestPoint);

                // Falls wir direkt darauf zufliegen, brauchen wir eine seitliche Komponente
                if (avoidanceDirection.length() < 0.1) {
                    avoidanceDirection = Vector.of(-velocity.y(), velocity.x());
                }

                totalSteer = totalSteer.add(avoidanceDirection.length(config.velocity));
                hits++;
            }
        }

        if (hits > 0) {
            // Durchschnittliche Ausweichrichtung berechnen
            Vector desiredVelocity = totalSteer.divide(hits).length(config.velocity);
            Vector steer = desiredVelocity.substract(velocity);
            physics.velocity = physics.velocity.add(steer.multiply(delta * 10));
        }
    }


    private static List<Entity> fetchNearbyBoids(Entity boid, List<Entity> boids, BoidComponent config) {


        final List<Entity> nearbyBoids = new ArrayList<>();
        for (final var other : boids) {
            if (other != boid && other.position().distanceTo(boid.position()) < config.visionRadius) {

                var directionVector = other.position().substract(boid.position()).normalize();
                var velocityNormalized = boid.get(PhysicsComponent.class).velocity.normalize();
                var dotProduct = directionVector.x() * velocityNormalized.x() + directionVector.y() * velocityNormalized.y();
                if (dotProduct > 0) {
                    nearbyBoids.add(other);
                }
            }
        }
        return nearbyBoids;
    }
}
