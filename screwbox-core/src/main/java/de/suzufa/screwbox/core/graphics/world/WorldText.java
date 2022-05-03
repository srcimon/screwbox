package de.suzufa.screwbox.core.graphics.world;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.graphics.Color;
import de.suzufa.screwbox.core.graphics.Font;

public record WorldText(Vector position, String text, Font font, Color color, boolean centered) {

    public static WorldText text(final Vector position, final String text, final Font font) {
        return text(position, text, font, Color.WHITE);
    }

    public static WorldText text(final Vector position, final String text, final Font font, final Color color) {
        return new WorldText(position, text, font, color, false);
    }

    public static WorldText centeredText(final Vector position, final String text, final Font font) {
        return centeredText(position, text, font, Color.WHITE);
    }

    public static WorldText centeredText(final Vector position, final String text, final Font font, final Color color) {
        return new WorldText(position, text, font, color, true);
    }
}
