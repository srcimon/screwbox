package io.github.simonbas.screwbox.core.entities.systems;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.Engine;
import io.github.simonbas.screwbox.core.entities.*;
import io.github.simonbas.screwbox.core.entities.components.ColliderComponent;
import io.github.simonbas.screwbox.core.entities.components.StaticMarkerComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;
import io.github.simonbas.screwbox.core.physics.internal.CollisionCheck;
import io.github.simonbas.screwbox.core.utils.GeometryUtil;

import java.util.List;
import java.util.Optional;

@Order(SystemOrder.OPTIMIZATION)
public class CombineStaticCollidersSystem implements EntitySystem {

    private static final Archetype COMBINABLES = Archetype.of(
            StaticMarkerComponent.class, ColliderComponent.class, TransformComponent.class);

    @Override
    public void update(final Engine engine) {
        final List<Entity> combinables = engine.entities().fetchAll(COMBINABLES);
        for (final var entity : combinables) {
            for (final var combinable : combinables) {
                var pair = new CollisionCheck(entity, combinable);
                if (pair.bodiesTouch() && pair.isNoSelfCollision() && tryToCombine(pair, engine)) {
                    return; // only one combination per frame
                }
            }
        }
        // at this point all colliders have been combined
        for (final var entity : combinables) {
            entity.remove(StaticMarkerComponent.class);
        }
        engine.entities().remove(CombineStaticCollidersSystem.class);
    }

    private boolean tryToCombine(final CollisionCheck check, final Engine engine) {
        final ColliderComponent colliderCollider = check.colliderComponent();
        final ColliderComponent bodyCollider = check.physics().get(ColliderComponent.class);
        if (!bodyCollider.bounce.equals(colliderCollider.bounce)
                || bodyCollider.friction != colliderCollider.friction
                || bodyCollider.isOneWay != colliderCollider.isOneWay) {
            return false;
        }

        final Optional<Bounds> combined = GeometryUtil.tryToCombine(check.colliderBounds(),
                check.physics().get(TransformComponent.class).bounds);
        if (combined.isEmpty()) {
            return false;
        }
        final ColliderComponent colliderComponent = new ColliderComponent(bodyCollider.friction,
                bodyCollider.bounce);
        colliderComponent.isOneWay = colliderCollider.isOneWay;
        final Entity newEntity = new Entity().add(
                colliderComponent,
                new StaticMarkerComponent(),
                new TransformComponent(combined.get()));

        engine.entities().add(newEntity);

        check.collider().remove(ColliderComponent.class);
        check.collider().remove(StaticMarkerComponent.class);

        check.physics().remove(ColliderComponent.class);
        check.physics().remove(StaticMarkerComponent.class);
        return true;
    }
}
