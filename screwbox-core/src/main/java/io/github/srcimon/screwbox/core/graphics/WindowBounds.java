package io.github.srcimon.screwbox.core.graphics;

import io.github.srcimon.screwbox.core.window.Window;

/**
 * Defines the area on the {@link Window}.
 */
public record WindowBounds(Offset offset, Dimension size) {

    public WindowBounds(final int x, final int y, final int width, final int height) {
        this(Offset.at(x, y), Dimension.of(width, height));
    }

    public Offset center() {
        final int x = offset.x() + size.width() / 2;
        final int y = offset.y() + size.height() / 2;
        return Offset.at(x, y);
    }

    /**
     * Returns {@code true} if the given {@link Offset} is within the
     * {@link WindowBounds}.
     * 
     * @param offset the {@link Offset} that is checked
     * @return {@code true} if the {@link Offset} is within
     */
    public boolean contains(final Offset offset) {
        return this.offset.x() <= offset.x()
                && this.offset.x() + size.width() >= offset.x()
                && this.offset.y() <= offset.y()
                && this.offset.y() + size.height() >= offset.y();
    }

    public boolean intersects(final WindowBounds other) {
        final var otherMinX = other.offset().x();
        final var otherMaxX = other.offset().x() + other.size().width();
        final var otherMinY = other.offset().y();
        final var otherMaxY = other.offset().y() + other.size().height();

        return offset.x() + size.width() > otherMinX
                && offset.x() < otherMaxX
                && offset.y() + size.height() > otherMinY
                && offset.y() < otherMaxY;
    }
}
