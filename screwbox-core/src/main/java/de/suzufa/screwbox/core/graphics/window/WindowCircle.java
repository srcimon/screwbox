package de.suzufa.screwbox.core.graphics.window;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Offset;

public record WindowCircle(Offset offset, int diameter, Color color, Percentage opacity) {

	public static WindowCircle circle(final Offset offset, final int diameter) {
		return circle(offset, diameter, Color.WHITE, Percentage.max());
	}

	public static WindowCircle circle(final Offset offset, final int diameter, final Percentage opacity) {
		return circle(offset, diameter, Color.WHITE, opacity);
	}

	public static WindowCircle circle(final Offset offset, final int diameter, final Color color) {
		return circle(offset, diameter, color, Percentage.max());
	}

	public static WindowCircle circle(final Offset offset, final int diameter, final Color color,
			final Percentage opacity) {
		return new WindowCircle(offset, diameter, color, opacity);
	}
}
