package de.suzufa.screwbox.core.graphics;

public final record WindowBounds(Offset offset, Dimension dimension) {

	public Offset center() {
		final int x = offset.x() + dimension.width() / 2;
		final int y = offset.y() + dimension.height() / 2;
		return Offset.at(x, y);
	}

	public boolean contains(final Offset offset) {
		return this.offset.x() <= offset.x()
				&& this.offset.x() + dimension.width() >= offset.x()
				&& this.offset.y() <= offset.y()
				&& this.offset.y() + dimension.height() >= offset.y();
	}

	public boolean intersects(final WindowBounds other) {
		final var otherMinX = other.offset().x();
		final var otherMaxX = other.offset().x() + other.dimension().width();
		final var otherMinY = other.offset().y();
		final var otherMaxY = other.offset().y() + other.dimension().height();

		return offset.x() + dimension.width() > otherMinX
				&& offset.x() < otherMaxX
				&& offset.y() + dimension.height() > otherMinY
				&& offset.y() < otherMaxY;
	}
}
