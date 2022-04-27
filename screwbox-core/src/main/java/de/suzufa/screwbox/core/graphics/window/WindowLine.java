package de.suzufa.screwbox.core.graphics.window;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Offset;

public record WindowLine(Offset from, Offset to, Color color, Percentage opacity) {

	public static WindowLine line(final Offset from, final Offset to) {
		return line(from, to, Color.WHITE);
	}

	public static WindowLine line(final Offset from, final Offset to, final Percentage opacity) {
		return line(from, to, Color.WHITE, opacity);
	}

	public static WindowLine line(final Offset from, final Offset to, final Color color) {
		return line(from, to, color, Percentage.max());
	}

	public static WindowLine line(final Offset from, final Offset to, final Color color, final Percentage opacity) {
		return new WindowLine(from, to, color, opacity);
	}
}
