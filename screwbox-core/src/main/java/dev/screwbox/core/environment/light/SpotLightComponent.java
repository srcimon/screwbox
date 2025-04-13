package dev.screwbox.core.environment.light;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

import java.io.Serial;

public class SpotLightComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double radius;
    public Color color;

    public SpotLightComponent(final double radius) {
        this(radius, Color.BLACK);
    }

    public SpotLightComponent(final double radius, final Color color) {
        this.radius = radius;
        this.color = color;
    }
}
