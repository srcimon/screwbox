package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.*;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.physics.internal.CollisionCheck;
import io.github.srcimon.screwbox.core.physics.internal.CollisionResolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Order(SystemOrder.SIMULATION_BEGIN)
public class PhysicsSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.of(PhysicsComponent.class, TransformComponent.class);
    private static final Archetype COLLIDERS = Archetype.of(TransformComponent.class, ColliderComponent.class);

    @Override
    public void update(final Engine engine) {
        final double factor = engine.loop().delta();
        final var colliders = engine.environment().fetchAll(COLLIDERS);
        for (final Entity entity : engine.environment().fetchAll(PHYSICS)) {
            final var physicsBody = entity.get(PhysicsComponent.class);
            final var transform = entity.get(TransformComponent.class);

            final Vector momentum = physicsBody.momentum.multiply(factor);
            transform.bounds = transform.bounds.moveBy(momentum);

            if (!physicsBody.ignoreCollisions) {
                applyCollisions(entity, colliders, factor);
            }
        }
    }

    private void applyCollisions(final Entity entity, final List<Entity> colliders, final double factor) {
        final List<CollisionCheck> collisionPairs = new ArrayList<>(colliders.size());
        for (final var collider : colliders) {
            if (entity != collider) {
                final CollisionCheck check = new CollisionCheck(entity, collider);
                if (check.bodiesIntersect() && check.isNoOneWayFalsePositive()) {
                    collisionPairs.add(check);
                }
            }
        }
        Collections.sort(collisionPairs);
        for (final var collisionPair : collisionPairs) {
            if (collisionPair.bodiesIntersect()) {
                CollisionResolver.resolveCollision(collisionPair, factor);
            }
        }
    }

}