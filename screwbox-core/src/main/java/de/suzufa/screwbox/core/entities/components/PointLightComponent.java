package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.entities.Component;

public class PointLightComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double range;

    public PointLightComponent(double range) {
        this.range = range;
    }
}
