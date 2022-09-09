package de.suzufa.screwbox.core.graphics;

//TODO: Test and javadoc
public final record WindowBounds(Offset offset, Dimension size) {

    public Offset center() {
        final int x = offset.x() + size.width() / 2;
        final int y = offset.y() + size.height() / 2;
        return Offset.at(x, y);
    }

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
