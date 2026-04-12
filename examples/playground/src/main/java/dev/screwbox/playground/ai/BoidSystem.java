package dev.screwbox.playground.ai;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import java.util.ArrayList;
import java.util.List;

public class BoidSystem implements EntitySystem {

    private static final Archetype BOIDS = Archetype.ofSpacial(PhysicsComponent.class, BoidComponent.class);
    private static final Archetype OBSTACLES = Archetype.ofSpacial(BoidObstacleComponent.class);

    @Override
    public void update(Engine engine) {
        var boids = engine.environment().fetchAll(BOIDS);
        double delta = engine.loop().delta();
        for (var boid : boids) {
            PhysicsComponent physics = boid.get(PhysicsComponent.class);
            final var config = boid.get(BoidComponent.class);
            if (physics.velocity.isZero()) {
                physics.velocity = Vector.random(config.velocity);
            }
            final List<Entity> nearbyBoids = fetchNearbyBoids(boid, boids, config);
            if (!nearbyBoids.isEmpty()) {
                applySeparation(boid, nearbyBoids, config, physics, delta);
                applyAlignment(nearbyBoids, config, physics, delta);
                applyCohesion(boid, nearbyBoids, config, physics, delta);
            }
            applyObstacleAvoidance(engine, boid, physics, delta);
            physics.velocity = physics.velocity.length(config.velocity);
        }


    }

    private static void applyCohesion(Entity boid, List<Entity> nearbyBoids, BoidComponent config, PhysicsComponent physics, double delta) {
        // 3. Cohesion (Mittelwert der Positionen -> Schwerpunkt)
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

    private static void applyAlignment(List<Entity> nearbyBoids, BoidComponent config, PhysicsComponent physics, double delta) {
        // 2. Alignment (Mittelwert der Geschwindigkeiten)
        var averageVelocity = Vector.zero();
        for (var nearbyBoid : nearbyBoids) {
            averageVelocity = averageVelocity.add(nearbyBoid.get(PhysicsComponent.class).velocity);
        }
        var desiredAlignementVelocity = averageVelocity.divide(nearbyBoids.size()).length(config.velocity);
        var alignmentSteer = desiredAlignementVelocity.substract(physics.velocity);
        physics.velocity = physics.velocity.add(alignmentSteer.multiply(config.alignmentStrenth * delta));
    }

    private static void applySeparation(Entity boid, List<Entity> nearbyBoids, BoidComponent config, PhysicsComponent physics, double delta) {
        var separationSteer = Vector.zero();
        // 1. separationSteer away from nearby boids (separation)
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

        physics.velocity = physics.velocity.add(separationSteer.multiply(config.separationStrength * delta));
    }

    private static void applyObstacleAvoidance(Engine engine, Entity boid, PhysicsComponent physics, double delta) {
        var config = boid.get(BoidComponent.class);
        double maxDistance = config.obstacleVisionRadius;

        List<Vector> rayTargets = List.of(
            physics.velocity.length(maxDistance),
            Angle.degrees(20).rotate(physics.velocity).length(maxDistance),
            Angle.degrees(-20).rotate(physics.velocity).length(maxDistance));

        Vector totalSteer = Vector.zero();
        int hits = 0;

        for (var targetOffset : rayTargets) {
            var hit = engine.navigation().raycastFrom(boid.position())
                .checkingFor(OBSTACLES)
                .castingTo(boid.position().add(targetOffset))
                .nearestEntity();

            if (hit.isPresent() && !hit.get().bounds().scale(1.25).contains(boid.bounds())) {// avoid getting boids stuck
                Bounds bounds = hit.get().bounds();

                // Berechne Fluchtpunkt (Weg von der nächsten Kante)
                Vector closestPoint = bounds.closestPoint(boid.position());

                Vector avoidanceDirection = boid.position().substract(closestPoint);

                // Falls wir direkt darauf zufliegen, brauchen wir eine seitliche Komponente
                if (avoidanceDirection.length() < 0.1) {
                    avoidanceDirection = Vector.of(-physics.velocity.y(), physics.velocity.x());
                }

                totalSteer = totalSteer.add(avoidanceDirection.length(config.velocity));
                hits++;
            }
        }

        if (hits > 0) {
            // Durchschnittliche Ausweichrichtung berechnen
            Vector desiredVelocity = totalSteer.divide(hits).length(config.velocity);
            Vector steer = desiredVelocity.substract(physics.velocity);
            physics.velocity = physics.velocity.add(steer.multiply(delta * config.obstacleAvoidanceStrength));
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
