package io.github.srcimon.screwbox.core.graphics;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

@Deprecated
//TODO remove from component
//TODO maybe remove from Light
public class LightOptions implements Serializable {

    private final double radius;
    private Color color = Color.BLACK;

    public static LightOptions radius(final double radius) {
        return new LightOptions(radius);
    }

    private LightOptions(final double radius) {
        //TODO: VALIDATE NO < 0
        this.radius = radius;
    }

    public LightOptions color(final Color color) {
        this.color = requireNonNull(color, "color must not be NULL");
        return this;
    }

    public LightOptions opacity(final double opacity) {
        //TODO validate
        color = color.opacity(opacity);
        return this;
    }

    public double radius() {
        return radius;
    }

    public Color color() {
        return color;
    }
}
