package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.entities.Component;
import de.suzufa.screwbox.core.graphics.Color;

public class PointLightComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double range;
    public Color color;

    public PointLightComponent(double range) {
        this(range, Color.BLACK);
    }

    public PointLightComponent(double range, Color color) {
        this.range = range;
        this.color = color;
    }
}
