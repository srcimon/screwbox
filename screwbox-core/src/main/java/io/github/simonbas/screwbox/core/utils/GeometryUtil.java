package io.github.simonbas.screwbox.core.utils;

import io.github.simonbas.screwbox.core.Bounds;

import java.util.Optional;

public final class GeometryUtil {

    private GeometryUtil() {
    }

    public static Optional<Bounds> tryToCombine(final Bounds bounds, final Bounds other) {
        if (sameVerticalLevel(bounds, other)) {
            if (horizontallyNextToEachOther(bounds, other)) {
                return Optional.of(Bounds.atOrigin(
                        Math.min(bounds.minX(), other.minX()),
                        other.minY(),
                        bounds.width() + other.width(),
                        other.height()));
            }
        } else if (sameHorizontalLevel(bounds, other) && verticallyNextToEachOther(bounds, other)) {
            return Optional.of(Bounds.atOrigin(
                    other.minX(),
                    Math.min(bounds.minY(), other.minY()),
                    other.width(),
                    bounds.height() + other.height()));
        }
        return Optional.empty();
    }

    private static boolean verticallyNextToEachOther(final Bounds bounds, final Bounds other) {
        return other.maxY() == bounds.minY() || other.minY() == bounds.maxY();
    }

    private static boolean horizontallyNextToEachOther(final Bounds bounds, final Bounds other) {
        return other.maxX() == bounds.minX() || other.minX() == bounds.maxX();
    }

    private static boolean sameHorizontalLevel(final Bounds bounds, final Bounds other) {
        return other.width() == bounds.width() && other.maxX() == bounds.maxX();
    }

    private static boolean sameVerticalLevel(final Bounds bounds, final Bounds other) {
        return other.height() == bounds.height() && other.maxY() == bounds.maxY();
    }
}
