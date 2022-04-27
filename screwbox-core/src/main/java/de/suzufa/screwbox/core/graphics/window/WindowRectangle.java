package de.suzufa.screwbox.core.graphics.window;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Dimension;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.WindowBounds;

public record WindowRectangle(WindowBounds bounds, Color color, Percentage opacity) {

	public static WindowRectangle dot(final Offset offset) {
		return dot(offset, Color.WHITE);
	}

	public static WindowRectangle dot(final Offset offset, final Color color) {
		return rectangle(offset, Dimension.of(1, 1), color);
	}

	public static WindowRectangle rectangle(final Offset offset, final Dimension dimension, final Color color) {
		return rectangle(offset, dimension, color, Percentage.max());
	}

	public static WindowRectangle rectangle(final Offset offset, final Dimension dimension, final Color color,
			final Percentage opacity) {
		final WindowBounds bounds = new WindowBounds(offset, dimension);
		return rectangle(bounds, color, opacity);
	}

	public static WindowRectangle rectangle(final WindowBounds bounds, final Color color, final Percentage opacity) {
		return new WindowRectangle(bounds, color, opacity);
	}

}
