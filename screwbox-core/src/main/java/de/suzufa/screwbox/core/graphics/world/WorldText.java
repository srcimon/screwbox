package de.suzufa.screwbox.core.graphics.world;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;

public record WorldText(Vector position, String text, Font font, Color color, Percentage opacity, boolean centered) {

    public static WorldText text(final Vector position, final String text, final Font font) {
        return text(position, text, font, Color.WHITE);
    }

    public static WorldText text(final Vector position, final String text, final Font font, final Color color) {
        return text(position, text, font, color, Percentage.max());
    }

    public static WorldText text(final Vector position, final String text, final Font font, final Percentage opacity) {
        return text(position, text, font, Color.WHITE, opacity);
    }

    public static WorldText text(final Vector position, final String text, final Font font, final Color color,
            final Percentage opacity) {
        return new WorldText(position, text, font, color, opacity, false);
    }

    public static WorldText centeredText(final Vector position, final String text, final Font font) {
        return centeredText(position, text, font, Color.WHITE);
    }

    public static WorldText centeredText(final Vector position, final String text, final Font font, final Color color) {
        return centeredText(position, text, font, color, Percentage.max());
    }

    public static WorldText centeredText(final Vector position, final String text, final Font font, final Color color,
            final Percentage opacity) {
        return new WorldText(position, text, font, color, opacity, true);
    }
}
