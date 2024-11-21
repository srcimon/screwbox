package io.github.srcimon.screwbox.core.physics.internal;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.physics.ColliderComponent;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsComponent;

import java.util.Objects;

public final class CollisionCheck implements Comparable<CollisionCheck> {

    private final Entity physics;
    private final Entity collider;
    private final Bounds colliderBounds;
    private final ColliderComponent colliderComponent;
    private final PhysicsComponent rigidBodyComponent;

    public CollisionCheck(final Entity physics, final Entity collider) {
        this.colliderBounds = collider.bounds();
        this.physics = physics;
        this.collider = collider;
        this.colliderComponent = collider.get(ColliderComponent.class);
        this.rigidBodyComponent = physics.get(PhysicsComponent.class);
    }

    public boolean bodiesIntersect() {
        return physicsBounds().intersects(colliderBounds);
    }

    public boolean bodiesTouch() {
        return physicsBounds().touches(colliderBounds);
    }

    public boolean isNoOneWayFalsePositive() {
        return !colliderComponent.isOneWay || (
                colliderBounds.position().y() - (colliderBounds.height() / 8) >= physicsBounds().maxY()
                        && rigidBodyComponent != null
                        && rigidBodyComponent.momentum.y() >= 0
                        && !rigidBodyComponent.ignoreOneWayCollisions);
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

    public Bounds physicsBounds() {
        return physics.bounds();
    }

    public PhysicsComponent physicsBodyComponent() {
        return rigidBodyComponent;
    }

    public ColliderComponent colliderComponent() {
        return colliderComponent;
    }

    int i = 0;
    public double overlap() {

        return physics.bounds().overlapArea(colliderBounds);
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

    public void movePhysics(final Vector movement) {
        physics.moveBy(movement);
    }
}
