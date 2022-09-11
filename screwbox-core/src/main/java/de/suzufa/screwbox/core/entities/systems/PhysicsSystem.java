package de.suzufa.screwbox.core.entities.systems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Engine;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Archetype;
import de.suzufa.screwbox.core.entities.Entity;
import de.suzufa.screwbox.core.entities.EntitySystem;
import de.suzufa.screwbox.core.entities.UpdatePriority;
import de.suzufa.screwbox.core.entities.components.ColliderComponent;
import de.suzufa.screwbox.core.entities.components.PhysicsBodyComponent;
import de.suzufa.screwbox.core.entities.components.TransformComponent;
import de.suzufa.screwbox.core.physics.internal.CollisionPair;
import de.suzufa.screwbox.core.physics.internal.CollisionResolver;

public class PhysicsSystem implements EntitySystem {

    private static final Archetype PHYSICS = Archetype.of(PhysicsBodyComponent.class, TransformComponent.class);
    private static final Archetype COLLIDERS = Archetype.of(TransformComponent.class, ColliderComponent.class);

    @Override
    public void update(final Engine engine) {
        final double factor = engine.loop().delta();
        final var colliders = engine.entityEngine().fetchAll(COLLIDERS);
        for (final Entity entity : engine.entityEngine().fetchAll(PHYSICS)) {
            final var currentMomentum = entity.get(PhysicsBodyComponent.class).momentum;
            final var transform = entity.get(TransformComponent.class);

            final Vector momentum = currentMomentum.multiply(factor);
            final Bounds updatedBounds = transform.bounds.moveBy(momentum);
            transform.bounds = updatedBounds;

            final List<CollisionPair> collisionPairs = new ArrayList<>(colliders.size());
            for (final var collider : colliders) {
                final CollisionPair check = new CollisionPair(entity, collider);
                if (check.bodiesIntersect() &&
                        check.checkWanted()
                        && check.isNoSelfCollision()
                        && check.isNoOneWayFalsePositive()) {
                    collisionPairs.add(check);
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

    @Override
    public UpdatePriority updatePriority() {
        return UpdatePriority.SIMULATION_BEGIN;
    }

}