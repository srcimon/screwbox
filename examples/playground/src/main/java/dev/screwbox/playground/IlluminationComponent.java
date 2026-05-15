package dev.screwbox.playground;

import dev.screwbox.core.environment.Component;

public class IlluminationComponent implements Component {

    public double radius;

    public IlluminationComponent(double radius) {
        this.radius = radius;
    }
}
