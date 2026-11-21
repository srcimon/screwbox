package dev.screwbox.core;

/**
 * Mixins bounds related functions to anything that has {@link Bounds}.
 *
 * @since 3.41.0
 */
@FunctionalInterface
public interface HasBounds {

    /**
     * Bounds of the mared object.
     */
    Bounds bounds();

    /**
     * Returns {@link Bounds#origin()}.
     */
    default Vector origin() {
        return bounds().origin();
    }

    /**
     * Returns {@link Bounds#position()}.
     */
    default Vector position() {
        return bounds().position();
    }

    /**
     * Checks if object intersects other.
     */
    default boolean intersects(final HasBounds other) {
        return intersects(other.bounds());
    }

    /**
     * Checks if object intersects {@link Bounds}.
     */
    default boolean intersects(final Bounds bounds) {
        return bounds().intersects(bounds);
    }

    /**
     * Checks if object touches {@link Bounds}.
     */
    default boolean touches(final HasBounds other) {
        return touches(other.bounds());
    }

    /**
     * Checks if object touches {@link Bounds}.
     */
    default boolean touches(final Bounds bounds) {
        return bounds().touches(bounds);
    }


}
