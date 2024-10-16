package io.github.srcimon.screwbox.core.graphics;

/**
 * Defines the area on the {@link Screen}.
 */
public record ScreenBounds(Offset offset, Size size) implements Sizeable {

    public ScreenBounds(final Size size) {
        this(Offset.origin(), size);
    }

    public ScreenBounds(final int x, final int y, final int width, final int height) {
        this(Offset.at(x, y), Size.of(width, height));
    }

    public Offset center() {
        final int x = offset.x() + size.width() / 2;
        final int y = offset.y() + size.height() / 2;
        return Offset.at(x, y);
    }

    /**
     * Returns {@code true} if the given {@link Offset} is within the
     * {@link ScreenBounds}.
     *
     * @param offset the {@link Offset} that is checked
     * @return {@code true} if the {@link Offset} is within
     */
    public boolean contains(final Offset offset) {
        return this.offset.x() <= offset.x()
                && this.offset.x() + width() >= offset.x()
                && this.offset.y() <= offset.y()
                && this.offset.y() + height() >= offset.y();
    }

    public boolean intersects(final ScreenBounds other) {
        final var otherMinX = other.offset().x();
        final var otherMaxX = other.offset().x() + other.width();
        final var otherMinY = other.offset().y();
        final var otherMaxY = other.offset().y() + other.height();

        return offset.x() + size.width() > otherMinX
                && offset.x() < otherMaxX
                && offset.y() + size.height() > otherMinY
                && offset.y() < otherMaxY;
    }
}
