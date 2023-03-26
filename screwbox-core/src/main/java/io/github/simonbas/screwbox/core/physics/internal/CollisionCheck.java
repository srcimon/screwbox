package io.github.simonbas.screwbox.core.physics.internal;

import io.github.simonbas.screwbox.core.Bounds;
import io.github.simonbas.screwbox.core.entities.Entity;
import io.github.simonbas.screwbox.core.entities.components.ColliderComponent;
import io.github.simonbas.screwbox.core.entities.components.PhysicsBodyComponent;
import io.github.simonbas.screwbox.core.entities.components.TransformComponent;

import java.util.Objects;

public final class CollisionCheck implements Comparable<CollisionCheck> {

    private final Entity physics;
    private final Entity collider;
    private final Bounds colliderBounds;
    private final ColliderComponent colliderComponent;
    private final PhysicsBodyComponent physicsBodyComponent;

    public CollisionCheck(final Entity physics, final Entity collider) {
        this.colliderBounds = collider.get(TransformComponent.class).bounds;
        this.physics = physics;
        this.collider = collider;
        this.colliderComponent = collider.get(ColliderComponent.class);
        this.physicsBodyComponent = physics.get(PhysicsBodyComponent.class);
    }

    public boolean checkWanted() {
        return !physicsBodyComponent.ignoreCollisions;
    }

    public boolean bodiesIntersect() {
        return physicsBounds().bounds.intersects(colliderBounds);
    }

    public boolean bodiesTouch() {
        return physicsBounds().bounds.touches(colliderBounds);
    }

    public boolean isNoOneWayFalsePositive() {
        return !colliderComponent.isOneWay || (
                colliderBounds.position().y() - (colliderBounds.height() / 8) >= physicsBounds().bounds.maxY()
                        && physicsBodyComponent != null
                        && physicsBodyComponent.momentum.y() >= 0
                        && !physicsBodyComponent.ignoreOneWayCollisions);
    }

    public boolean isNoSelfCollision() {
        return physics != collider;
    }

    public Entity collider() {
        return collider;
    }

    public Entity physics() {
        return physics;
    }

    public Bounds colliderBounds() {
        return colliderBounds;
    }

    public TransformComponent physicsBounds() {
        return physics.get(TransformComponent.class);
    }

    public PhysicsBodyComponent physicsBodyComponent() {
        return physicsBodyComponent;
    }

    public ColliderComponent colliderComponent() {
        return colliderComponent;
    }

    public double overlap() {
        return physicsBounds().bounds.overlapArea(colliderBounds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collider, physics);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CollisionCheck other = (CollisionCheck) obj;
        return Objects.equals(collider, other.collider) && Objects.equals(physics, other.physics);
    }

    @Override
    public int compareTo(final CollisionCheck other) {
        return Double.compare(other.overlap(), overlap());
    }

}
