package dev.screwbox.core.navigation.internal;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.physics.ColliderComponent;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import java.util.Objects;

public final class CollisionCheck implements Comparable<CollisionCheck> {

    private final Entity physics;
    private final Entity collider;
    private final Bounds colliderBounds;
    private final ColliderComponent colliderComponent;
    private final PhysicsComponent physicsComponent;

    public CollisionCheck(final Entity physics, final Entity collider) {
        this.colliderBounds = collider.bounds();
        this.physics = physics;
        this.collider = collider;
        this.colliderComponent = collider.get(ColliderComponent.class);
        this.physicsComponent = physics.get(PhysicsComponent.class);
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
                        && physicsComponent != null
                        && physicsComponent.velocity.y() >= 0
                        && !physicsComponent.ignoreOneWayCollisions);
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
        return physicsComponent;
    }

    public ColliderComponent colliderComponent() {
        return colliderComponent;
    }

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
