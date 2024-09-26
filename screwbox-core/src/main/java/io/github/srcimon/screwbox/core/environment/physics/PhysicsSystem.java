package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.environment.core.TransformComponent;
import io.github.srcimon.screwbox.core.physics.internal.CollisionCheck;
import io.github.srcimon.screwbox.core.physics.internal.CollisionResolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Order(Order.SystemOrder.SIMULATION_BEGIN)
public class PhysicsSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.of(PhysicsComponent.class, TransformComponent.class);
    private static final Archetype COLLIDERS = Archetype.of(TransformComponent.class, ColliderComponent.class);

    @Override
    public void update(final Engine engine) {
        final double factor = engine.loop().delta();
        final var colliders = engine.environment().fetchAll(COLLIDERS);
        for (final Entity entity : engine.environment().fetchAll(PHYSICS)) {
            final var physicsBody = entity.get(PhysicsComponent.class);
            entity.moveBy(physicsBody.momentum.multiply(factor));

            if (!physicsBody.ignoreCollisions) {
                applyCollisions(entity, colliders, factor);
            }
        }
    }

    private void applyCollisions(final Entity entity, final List<Entity> colliders, final double factor) {
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
            Collections.sort(collisionChecks);
        }
        for (final var collisionCheck : collisionChecks) {
            if (collisionCheck.bodiesIntersect()) {
                CollisionResolver.resolveCollision(collisionCheck, factor);
            }
        }
    }

}