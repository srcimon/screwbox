package de.suzufa.screwbox.core.graphics.window;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.graphics.Offset;
import de.suzufa.screwbox.core.graphics.Sprite;

public record WindowRepeatingSprite(Offset offset, Sprite sprite, double scale, Percentage opacity) {

	public static WindowRepeatingSprite repeatingSprite(final Sprite sprite) {
		return repeatingSprite(Offset.origin(), sprite, 1);
	}

	public static WindowRepeatingSprite repeatingSprite(final Sprite sprite, final double scale) {
		return repeatingSprite(Offset.origin(), sprite, scale);
	}

	public static WindowRepeatingSprite repeatingSprite(final Offset offset, final Sprite sprite, final double scale) {
		return repeatingSprite(offset, sprite, scale, Percentage.max());
	}

	public static WindowRepeatingSprite repeatingSprite(final Offset offset, final Sprite sprite, final double scale,
			final Percentage opacity) {
		return new WindowRepeatingSprite(offset, sprite, scale, opacity);
	}
}
