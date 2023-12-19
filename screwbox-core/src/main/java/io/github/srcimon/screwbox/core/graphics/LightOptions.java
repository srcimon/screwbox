package io.github.srcimon.screwbox.core.graphics;

import java.io.Serializable;

public record LightOptions(double radius, Color color, double glow, Color glowColor) implements Serializable {

    public static LightOptions glowing(final double radius) {
        return new LightOptions(radius, Color.BLACK, 1.0, Color.WHITE.opacity(0.2));
    }

    public static LightOptions noGlow(final double radius) {
        return new LightOptions(radius, Color.BLACK, 0, Color.WHITE);
    }

    public LightOptions radius(final double radius) {
        return new LightOptions(radius, color, glow, glowColor);
    }

    public LightOptions glow(final double glow) {
        return new LightOptions(radius, color, glow, glowColor);
    }

    public LightOptions color(final Color color) {
        return new LightOptions(radius, color, glow, glowColor);
    }

    public LightOptions glowColor(final Color glowColor) {
        return new LightOptions(radius, color, glow, glowColor);
    }
}
