package de.suzufa.screwbox.core.physics.internal;

import java.util.Objects;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entityengine.Entity;
import de.suzufa.screwbox.core.entityengine.components.TransformComponent;
import de.suzufa.screwbox.core.entityengine.components.ColliderComponent;
import de.suzufa.screwbox.core.entityengine.components.PhysicsBodyComponent;

//TODO: Refactor: I hate this class
public final class CollisionPair implements Comparable<CollisionPair> {

    private final Entity physics;
    private final Entity collider;
    private final Bounds colliderBounds;
    private final ColliderComponent colliderComponent;
    private final PhysicsBodyComponent physicsBodyComponent;

    public CollisionPair(final Entity physics, final Entity collider) {
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
        return !colliderComponent.isOneWay || (colliderComponent.isOneWay
                && colliderBounds.position().y() - (colliderBounds.height() / 8) >= physicsBounds().bounds.maxY()
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CollisionPair other = (CollisionPair) obj;
        return Objects.equals(collider, other.collider) && Objects.equals(physics, other.physics);
    }

    @Override
    public int compareTo(final CollisionPair other) {
        return Double.compare(other.overlap(), overlap());
    }

}
