package dev.screwbox.playground.ai;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.Line;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.navigation.SpacialAdaptiveIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static dev.screwbox.core.environment.Order.SIMULATION_EARLY;

@ExecutionOrder(SIMULATION_EARLY)
public class BoidSystem implements EntitySystem {

    private static final Archetype BOIDS = Archetype.ofSpacial(PhysicsComponent.class, BoidComponent.class);
    private static final Archetype OBSTACLES = Archetype.ofSpacial(BoidObstacleComponent.class);

    private final SpacialAdaptiveIndex spacialAdaptiveIndex = new SpacialAdaptiveIndex();

    @Override
    public void update(Engine engine) {
        final var boids = engine.environment().fetchAll(BOIDS);
        if (boids.isEmpty()) {
            return;
        }

        final var obstacles = engine.environment().fetchAll(OBSTACLES);
        double delta = engine.loop().delta();
        spacialAdaptiveIndex.refresh(boids);

        boids.parallelStream().forEach(boid -> {
            PhysicsComponent physics = boid.get(PhysicsComponent.class);
            final var config = boid.get(BoidComponent.class);
            if (physics.velocity.isZero()) {
                physics.velocity = Vector.random(config.velocity);
            }
            final Predicate<Entity> entityFilter = entity -> entity != boid && !config.perceptFrontalOnly || isFrontal(boid, entity);
            final List<Entity> nearbyBoids = spacialAdaptiveIndex.findEntities(boid.position(), config.perceptionRadius, entityFilter);

            if (!nearbyBoids.isEmpty()) {

                applySeparation(boid, nearbyBoids, config, physics, delta);
                applyAlignment(nearbyBoids, config, physics, delta);
                applyCohesion(boid, nearbyBoids, config, physics, delta);
            }
            applyObstacleAvoidance(boid, physics, obstacles, delta);
            physics.velocity = physics.velocity.length(config.velocity);
        });
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

    private static void applyObstacleAvoidance(Entity boid, PhysicsComponent physics, List<Entity> obstacles, double delta) {
        var config = boid.get(BoidComponent.class);

        var normal = Line.normal(boid.position(), config.obstaclePerceptionRadius);
        Angle angle = Angle.ofVector(physics.velocity.invert());


        List<Bounds> inTheWayObstacles = new ArrayList<>();//TODO get out if no obstacle
        //TODO also check only nearby
        for (var obstacle : obstacles) {
            if (obstacle.get(BoidObstacleComponent.class).isContainer == obstacle.bounds().scale(1.1).contains(boid.bounds())) {
                for (var border : obstacle.bounds().borders()) {
                    List<Line> rayTargets = List.of(
                        angle.rotate(normal),
                        angle.addDegrees(-20).rotate(normal),
                        angle.addDegrees(20).rotate(normal));//TODO config 20

                    for (var ray : rayTargets) {
                        if (border.intersects(ray)) {
                            inTheWayObstacles.add(obstacle.bounds());
                        }
                    }
                }
            }
        }


        Vector totalSteer = Vector.zero();
        int hits = 0;
        for (var obstacle : inTheWayObstacles) {
            Vector closestPoint = obstacle.closestPoint(boid.position());

            Vector avoidanceDirection = boid.position().substract(closestPoint);

            // Falls wir direkt darauf zufliegen, brauchen wir eine seitliche Komponente
            if (avoidanceDirection.length() < 0.1) {
                avoidanceDirection = Vector.of(-physics.velocity.y(), physics.velocity.x());
            }

            totalSteer = totalSteer.add(avoidanceDirection.length(config.velocity));
            hits++;
        }


        if (hits > 0) {
            // Durchschnittliche Ausweichrichtung berechnen
            Vector desiredVelocity = totalSteer.divide(hits).length(config.velocity);
            Vector steer = desiredVelocity.substract(physics.velocity);
            physics.velocity = physics.velocity.add(steer.multiply(delta * config.obstacleAvoidanceStrength));
        }
    }


    private static boolean isFrontal(Entity boid, Entity entity) {
        final var directionVector = entity.position().substract(boid.position());
        final var velocity = entity.get(PhysicsComponent.class).velocity;
        return directionVector.normalizedDotProduct(velocity) > 0;
    }

}
