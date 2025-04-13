package dev.screwbox.core.environment.light;

import dev.screwbox.core.Rotation;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.Color;

import java.io.Serial;

public class ConeLightComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double radius;
    public Color color;
    public Rotation direction;
    public Rotation cone;

    public ConeLightComponent(final Rotation direction, final Rotation cone, final double radius) {
        this(direction, cone, radius, Color.BLACK);
    }

    public ConeLightComponent(final Rotation direction, final Rotation cone, final double radius, final Color color) {
        this.direction = direction;
        this.cone = cone;
        this.radius = radius;
        this.color = color;
    }
}
