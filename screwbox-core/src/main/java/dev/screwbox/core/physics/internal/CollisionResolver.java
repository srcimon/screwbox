package dev.screwbox.core.physics.internal;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.physics.PhysicsComponent;
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
        if (MathUtil.sameSign(correction.x(), physicsBodyComponent.momentum.x())) {
            return;
        }
        physicsBodyComponent.momentum = physicsBodyComponent.momentum.replaceX(
                physicsBodyComponent.momentum.x() * -1 * colliderComponent.bounce.value());
    }

    private static void reactOnVerticalCollision(final CollisionCheck pair, final double updateFactor) {
        final var physicsBodyComponent = pair.physicsBodyComponent();
        final var colliderComponent = pair.colliderComponent();
        var cc = pair.collider().get(PhysicsComponent.class);
        final Vector newMomentum = cc == null ? physicsBodyComponent.momentum.replaceY(
                physicsBodyComponent.momentum.y() * -1 * colliderComponent.bounce.value())
                : getVector(physicsBodyComponent, cc);

        final double absX = abs(newMomentum.x());
        final double friction = Math.min(absX, colliderComponent.friction * updateFactor);

        physicsBodyComponent.momentum = newMomentum.addX(MathUtil.modifier(newMomentum.x()) * friction * -1);
    }

    private static Vector getVector(PhysicsComponent physicsBodyComponent, PhysicsComponent cc) {
        return physicsBodyComponent.momentum.replaceY(cc.momentum.y() > 0
                ? cc.momentum.y() * 1.25
                : cc.momentum.y());
    }
    //TODO freaking double double check this!
}
