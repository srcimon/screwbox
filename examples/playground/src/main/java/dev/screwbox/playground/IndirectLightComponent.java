package dev.screwbox.playground;

import dev.screwbox.core.environment.Component;

public class IndirectLightComponent implements Component {

    public double radius;

    public IndirectLightComponent(double radius) {
        this.radius = radius;
    }
}
