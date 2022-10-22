package de.suzufa.screwbox.core.graphics;

import java.io.Serializable;

public record LightOptions(double size, Color color, double glow, Color glowColor) implements Serializable {

    public static LightOptions glowing(double size) {
        return new LightOptions(size, Color.BLACK, 1.0, Color.WHITE);
    }

    public static LightOptions noGlow(double size) {
        return new LightOptions(size, Color.BLACK, 0, Color.WHITE);
    }

    public LightOptions resize(double size) {
        return new LightOptions(size, color, glow, glowColor);
    }

    public LightOptions glow(double glow) {
        return new LightOptions(size, color, glow, glowColor);
    }

    public LightOptions color(Color color) {
        return new LightOptions(size, color, glow, glowColor);
    }

    public LightOptions glowColor(Color glowColor) {
        return new LightOptions(size, color, glow, glowColor);
    }

}
