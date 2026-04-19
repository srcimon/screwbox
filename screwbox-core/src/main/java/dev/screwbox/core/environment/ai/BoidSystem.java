package dev.screwbox.core.environment.ai;

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
import dev.screwbox.core.navigation.SpacialIndex;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static dev.screwbox.core.environment.Order.SIMULATION_EARLY;

@ExecutionOrder(SIMULATION_EARLY)
public class BoidSystem implements EntitySystem {

    private static final Archetype BOIDS = Archetype.ofSpacial(PhysicsComponent.class, BoidComponent.class);
    private static final Archetype OBSTACLES = Archetype.ofSpacial(BoidObstacleComponent.class);

    private final SpacialIndex spacialIndex = new SpacialIndex();

    @Override
    public void update(final Engine engine) {
        final var boids = engine.environment().fetchAll(BOIDS);
        if (boids.isEmpty()) {
            return;
        }

        final var obstacles = engine.environment().fetchAll(OBSTACLES);
        double delta = engine.loop().delta();
        spacialIndex.refresh(boids);

        boids.parallelStream().forEach(boid -> {
            final var physics = boid.get(PhysicsComponent.class);
            final var config = boid.get(BoidComponent.class);
            if (physics.velocity.isZero()) {
                physics.velocity = Vector.random(config.velocity);
            }
            final Predicate<Entity> entityFilter = entity -> entity != boid && !config.perceptFrontalOnly || isFrontal(boid, entity);
            final List<Entity> nearbyBoids = spacialIndex.findEntities(boid.position(), config.perceptionRadius, entityFilter);

            if (!nearbyBoids.isEmpty()) {
                applySeparation(boid, nearbyBoids, config, physics, delta);
                applyAlignment(nearbyBoids, config, physics, delta);
                applyCohesion(boid, nearbyBoids, config, physics, delta);
            }
            if (!obstacles.isEmpty()) {
                applyObstacleAvoidance(boid, physics, obstacles, delta);
            }
            physics.velocity = physics.velocity.length(config.velocity);
        });
    }

    private static void applyCohesion(Entity boid, List<Entity> nearbyBoids, BoidComponent config, PhysicsComponent physics, double delta) {
        var centerPos = calclulateCenterOfMass(nearbyBoids);
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

        final var normal = Line.normal(boid.position(), config.obstaclePerceptionRadius);
        final Angle angle = Angle.ofVector(physics.velocity.invert());

        final List<Bounds> inTheWayObstacles = new ArrayList<>();
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
            final Vector closestPoint = obstacle.closestPoint(boid.position());
            Vector avoidanceDirection = boid.position().substract(closestPoint);
            if (avoidanceDirection.length() < 0.1) {
                avoidanceDirection = Vector.of(-physics.velocity.y(), physics.velocity.x());
            }

            totalSteer = totalSteer.add(avoidanceDirection.length(config.velocity));
            hits++;
        }


        if (hits > 0) {
            final Vector desiredVelocity = totalSteer.divide(hits).length(config.velocity);
            final Vector steer = desiredVelocity.substract(physics.velocity);
            physics.velocity = physics.velocity.add(steer.multiply(delta * config.obstacleAvoidanceStrength));
        }
    }


    private static boolean isFrontal(final Entity boid, final Entity perceptedEntity) {
        final var directionVector = perceptedEntity.position().substract(boid.position());
        final var velocity = perceptedEntity.get(PhysicsComponent.class).velocity;
        return directionVector.normalizedDotProduct(velocity) > 0;
    }

    private static Vector calclulateCenterOfMass(final List<Entity> boids) {
        double x = 0;
        double y = 0;
        for (final var boid : boids) {
            x += boid.position().x();
            y += boid.position().y();
        }
        return Vector.of(x, y);
    }
}
