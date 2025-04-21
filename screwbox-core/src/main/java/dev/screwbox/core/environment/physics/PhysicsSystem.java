package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.physics.internal.CollisionCheck;
import dev.screwbox.core.physics.internal.CollisionResolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Order(Order.SystemOrder.SIMULATION_EARLY)
public class PhysicsSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.ofSpacial(PhysicsComponent.class);
    private static final Archetype COLLIDERS = Archetype.ofSpacial(ColliderComponent.class);

    @Override
    public void update(final Engine engine) {
        final double delta = engine.loop().delta();
        final var colliders = engine.environment().fetchAll(COLLIDERS);
        for (final Entity entity : engine.environment().fetchAll(PHYSICS).stream().sorted(Comparator.comparing(p -> -p.bounds().minY())).toList()) {
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
        final Bounds entityBounds = entity.bounds();
        for (final var collider : colliders) {
            if (entity != collider && entityBounds.intersects(collider.bounds())) {
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