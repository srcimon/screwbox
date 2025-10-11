package dev.screwbox.core.navigation.internal;

import dev.screwbox.core.Vector;
import dev.screwbox.core.utils.MathUtil;

import static java.lang.Math.abs;

public final class CollisionResolver {

    private CollisionResolver() {
    }

    public static void resolveCollision(final CollisionCheck check, final double updateFactor) {
        final Vector correction = getResolveVector(check);
        if (correction.x() == 0) {
            reactOnVerticalCollision(check, updateFactor);
        } else {
            reactOnHorizontalCollision(check, correction);
        }
        check.movePhysics(correction);
    }

    private static Vector getResolveVector(final CollisionCheck check) {
        final var colliderBounds = check.colliderBounds();
        final var entityBounds = check.physicsBounds();

        final boolean colliderBelowPhysics = abs(colliderBounds.maxY() - entityBounds.minY()) < abs(
                colliderBounds.minY() - entityBounds.maxY());

        final double deltaY = colliderBelowPhysics
                ? colliderBounds.maxY() - entityBounds.minY()
                : colliderBounds.minY() - entityBounds.maxY();

        final boolean colliderLeftOfPhysics = abs(colliderBounds.maxX() - entityBounds.minX()) < abs(
                colliderBounds.minX() - entityBounds.maxX());

        final double deltaX = colliderLeftOfPhysics
                ? colliderBounds.maxX() - entityBounds.minX()
                : colliderBounds.minX() - entityBounds.maxX();

        return abs(deltaY) < abs(deltaX)
                ? Vector.y(deltaY)
                : Vector.x(deltaX);
    }

    private static void reactOnHorizontalCollision(final CollisionCheck pair, final Vector correction) {
        final var physicsBodyComponent = pair.physicsBodyComponent();
        final var colliderComponent = pair.colliderComponent();
        // prevents getting stuck when going off a cliff
        if (MathUtil.sameSign(correction.x(), physicsBodyComponent.velocity.x())) {
            return;
        }
        physicsBodyComponent.velocity = physicsBodyComponent.velocity.replaceX(
                physicsBodyComponent.velocity.x() * -1 * colliderComponent.bounce.value());
    }

    private static void reactOnVerticalCollision(final CollisionCheck pair, final double updateFactor) {
        final var physicsBodyComponent = pair.physicsBodyComponent();
        final var colliderComponent = pair.colliderComponent();
        final Vector updatedVelocity = physicsBodyComponent.velocity.replaceY(
                physicsBodyComponent.velocity.y() * -1 * colliderComponent.bounce.value());

        final double absX = abs(updatedVelocity.x());
        final double friction = Math.min(absX, colliderComponent.friction * updateFactor);

        physicsBodyComponent.velocity = updatedVelocity.addX(MathUtil.modifier(updatedVelocity.x()) * friction * -1);
    }
}
