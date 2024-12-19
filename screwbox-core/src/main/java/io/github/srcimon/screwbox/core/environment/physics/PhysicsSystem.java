package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Engine;
import io.github.srcimon.screwbox.core.environment.Archetype;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.Order;
import io.github.srcimon.screwbox.core.physics.internal.CollisionCheck;
import io.github.srcimon.screwbox.core.physics.internal.CollisionResolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Order(Order.SystemOrder.SIMULATION_EARLY)
public class PhysicsSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.ofSpacial(PhysicsComponent.class);
    private static final Archetype COLLIDERS = Archetype.ofSpacial(ColliderComponent.class);

    @Override
    public void update(final Engine engine) {
        final double delta = engine.loop().delta();
        final var colliders = engine.environment().fetchAll(COLLIDERS);
        for (final Entity entity : engine.environment().fetchAll(PHYSICS)) {
            final var physicsBody = entity.get(PhysicsComponent.class);
            entity.moveBy(physicsBody.momentum.multiply(delta));

            if (!physicsBody.ignoreCollisions) {
                for (final var collisionCheck : fetchOrderedCollisionChecks(entity, colliders)) {
                    if (collisionCheck.bodiesIntersect()) {
                        CollisionResolver.resolveCollision(collisionCheck, delta);
                    }
                }
            }
        }
    }

    private List<CollisionCheck> fetchOrderedCollisionChecks(final Entity entity, final List<Entity> colliders) {
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
        return collisionChecks;
    }

}