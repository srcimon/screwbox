package dev.screwbox.core;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.TransformComponent;

@FunctionalInterface
public interface HasBounds {

    Bounds bounds();

    /**
     * Returns {@link Bounds#origin()} if {@link Entity} has {@link TransformComponent}.
     *
     * @throws IllegalStateException if {@link Entity} has no {@link TransformComponent}
     */
    default  Vector origin() {
        return bounds().origin();
    }

    /**
     * Returns {@link Bounds#position()} if {@link Entity} has {@link TransformComponent}.
     *
     * @throws IllegalStateException if {@link Entity} has no {@link TransformComponent}
     */
    default Vector position() {
        return bounds().position();
    }

    default boolean intersects(HasBounds other) {
        return bounds().intersects(other.bounds());
    }

    default boolean intersects(Bounds bounds) {
        return bounds().intersects(bounds);
    }

    default boolean touches(HasBounds other) {
        return bounds().touches(other.bounds());
    }

    default boolean touches(Bounds bounds) {
        return bounds().touches(bounds);
    }


}
