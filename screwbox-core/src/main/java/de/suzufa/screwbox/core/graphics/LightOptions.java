package de.suzufa.screwbox.core.graphics;

import java.io.Serializable;

public record LightOptions(double size, Color color, double glow, Color glowColor) implements Serializable {

    public static LightOptions glowing(final double size) {
        return new LightOptions(size, Color.BLACK, 1.0, Color.WHITE.opacity(0.2));
    }

    public static LightOptions noGlow(final double size) {
        return new LightOptions(size, Color.BLACK, 0, Color.WHITE);
    }

    public LightOptions resize(final double size) {
        return new LightOptions(size, color, glow, glowColor);
    }

    public LightOptions glow(final double glow) {
        return new LightOptions(size, color, glow, glowColor);
    }

    public LightOptions color(final Color color) {
        return new LightOptions(size, color, glow, glowColor);
    }

    public LightOptions glowColor(final Color glowColor) {
        return new LightOptions(size, color, glow, glowColor);
    }

}
