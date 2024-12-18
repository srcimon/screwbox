package io.github.srcimon.screwbox.core.graphics;

/**
 * Defines the area on the {@link Screen}.
 */
public record ScreenBounds(Offset offset, Size size) implements Sizeable {

    /**
     * Creates a new instance at {@link Offset#origin()}.
     */
    public ScreenBounds(final Size size) {
        this(Offset.origin(), size);
    }

    /**
     * Creates a new instance.
     */
    public ScreenBounds(final int x, final int y, final int width, final int height) {
        this(Offset.at(x, y), Size.of(width, height));
    }

    /**
     * Returns the center {@link Offset}.
     */
    public Offset center() {
        final int x = offset.x() + size.width() / 2;
        final int y = offset.y() + size.height() / 2;
        return Offset.at(x, y);
    }

    /**
     * Returns {@code true} if the specified {@link Offset} is within the
     * {@link ScreenBounds}.
     *
     * @param other the {@link Offset} that is checked
     * @return {@code true} if the {@link Offset} is within
     */
    public boolean contains(final Offset other) {
        return offset.x() <= other.x()
                && offset.x() + width() >= other.x()
                && offset.y() <= other.y()
                && offset.y() + height() >= other.y();
    }

    /**
     * Returns {@code true} if specified other {@link ScreenBounds} intersects this.
     */
    public boolean intersects(final ScreenBounds other) {
        return offset.x() + size.width() > other.offset().x()
                && offset.x() < other.offset().x() + other.width()
                && offset.y() + size.height() > other.offset().y()
                && offset.y() < other.offset().y() + other.height();
    }
}
