package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.utils.internal.CollisionCheck;
import dev.screwbox.core.utils.internal.CollisionResolver;

import java.util.ArrayList;
import java.util.List;

import static dev.screwbox.core.environment.Order.SIMULATION_EARLY;

@ExecutionOrder(SIMULATION_EARLY)
public class PhysicsSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.ofSpacial(PhysicsComponent.class);
    private static final Archetype COLLIDERS = Archetype.ofSpacial(ColliderComponent.class);

    @Override
    public void update(final Engine engine) {
        final double delta = engine.loop().delta();
        final var colliders = engine.environment().fetchAll(COLLIDERS);
        for (final Entity entity : engine.environment().fetchAll(PHYSICS)) {
            final var physicsBody = entity.get(PhysicsComponent.class);
            physicsBody.velocity = physicsBody.velocity.limit(physicsBody.maxVelocity);
            entity.moveBy(physicsBody.velocity.multiply(delta));

            if (!physicsBody.ignoreCollisions) {
                for (final var collisionCheck : fetchOrderedCollisionChecks(entity, colliders)) {
                    if (collisionCheck.bodiesIntersect()) {
                        CollisionResolver.resolveCollision(collisionCheck, delta);
                    }
                }
            }
            physicsBody.velocity = physicsBody.velocity.reduce(physicsBody.velocity.length() * physicsBody.friction * delta);
        }
    }

    private static List<CollisionCheck> fetchOrderedCollisionChecks(final Entity entity, final List<Entity> colliders) {
        final List<CollisionCheck> collisionChecks = new ArrayList<>();
        for (final var collider : colliders) {
            if (entity != collider && entity.bounds().intersects(collider.bounds())) {
                final CollisionCheck check = new CollisionCheck(entity, collider);
                if (check.isNoOneWayFalsePositive()) {
                    collisionChecks.add(check);
                }
            }
        }
        if (collisionChecks.size() > 1) {
            collisionChecks.sort(null);
        }
        return collisionChecks;
    }

}