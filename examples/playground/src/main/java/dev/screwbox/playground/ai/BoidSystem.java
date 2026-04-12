package dev.screwbox.playground.ai;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.physics.PhysicsComponent;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.options.LineDrawOptions;
import dev.screwbox.core.graphics.options.OvalDrawOptions;

import java.util.ArrayList;
import java.util.List;

public class BoidSystem implements EntitySystem {

    private static final Archetype BOIDS = Archetype.ofSpacial(PhysicsComponent.class, BoidComponent.class);

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
            appySeparation(boid, config, nearbyBoids, engine.loop().delta());
        }
        // 1. steer away from nearby boids (separation)
        // 2. steer in same direction as nearby boids (alignment)
        // 3. steer towards center of nearby boids (cohesion)
    }

    private void appySeparation(Entity boid, BoidComponent config, List<Entity> nearbyBoids, double delta) {
        PhysicsComponent physics = boid.get(PhysicsComponent.class);
        var currentVelocity = physics.velocity;
        double strength = config.steeringStrength * delta;
        for (final var nearbyBoid : nearbyBoids) {
            var desiredDirection = boid.position().substract(nearbyBoid.position());
            var desiredVelocity = desiredDirection.normalize().multiply(config.velocity);
            var steer = desiredVelocity.substract(currentVelocity);
            physics.velocity = currentVelocity.add(steer.multiply(strength)).length(config.velocity);
        }

    }

    private static List<Entity> fetchNearbyBoids(Entity boid, List<Entity> boids, BoidComponent config) {
        final List<Entity> nearbyBoids = new ArrayList<>();
        for (final var other : boids) {
            if (other != boid && other.position().distanceTo(boid.position()) < config.visionRadius) {
                nearbyBoids.add(other);
            }
        }
        return nearbyBoids;
    }
}
