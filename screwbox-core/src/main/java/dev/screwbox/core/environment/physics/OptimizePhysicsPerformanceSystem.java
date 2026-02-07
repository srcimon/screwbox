package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Engine;
import dev.screwbox.core.environment.Archetype;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ExecutionOrder;
import dev.screwbox.core.environment.Order;
import dev.screwbox.core.environment.core.TransformComponent;
import dev.screwbox.core.utils.internal.CollisionCheck;
import dev.screwbox.core.utils.GeometryUtil;

import java.util.List;
import java.util.Optional;

@ExecutionOrder(Order.OPTIMIZATION)
public class OptimizePhysicsPerformanceSystem implements EntitySystem {

    private static final Archetype COMBINABLES = Archetype.of(
            StaticColliderComponent.class, ColliderComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final List<Entity> combinables = engine.environment().fetchAll(COMBINABLES);
        for (final var entity : combinables) {
            for (final var combinable : combinables) {
                if (entity != combinable) {
                    var pair = new CollisionCheck(entity, combinable);
                    if (pair.bodiesTouch() && tryToCombine(pair, engine)) {
                        return; // only one combination per frame
                    }
                }
            }
        }
        // at this point all colliders have been combined
        for (final var entity : combinables) {
            entity.remove(StaticColliderComponent.class);
        }
        engine.environment().remove(OptimizePhysicsPerformanceSystem.class);
    }

    private static boolean tryToCombine(final CollisionCheck check, final Engine engine) {
        final ColliderComponent colliderCollider = check.colliderComponent();
        final ColliderComponent bodyCollider = check.physics().get(ColliderComponent.class);
        if (!bodyCollider.bounce.equals(colliderCollider.bounce)
                || bodyCollider.friction != colliderCollider.friction
                || bodyCollider.isOneWay != colliderCollider.isOneWay) {
            return false;
        }

        final Optional<Bounds> combined = GeometryUtil.tryToCombine(check.colliderBounds(), check.physics().bounds());
        if (combined.isEmpty()) {
            return false;
        }
        final ColliderComponent colliderComponent = new ColliderComponent(bodyCollider.friction,
                bodyCollider.bounce);
        colliderComponent.isOneWay = colliderCollider.isOneWay;
        final Entity newEntity = new Entity().add(
                colliderComponent,
                new StaticColliderComponent(),
                new TransformComponent(combined.get()));

        engine.environment().addEntity(newEntity);

        check.collider().remove(ColliderComponent.class);
        check.collider().remove(StaticColliderComponent.class);

        check.physics().remove(ColliderComponent.class);
        check.physics().remove(StaticColliderComponent.class);
        return true;
    }
}
