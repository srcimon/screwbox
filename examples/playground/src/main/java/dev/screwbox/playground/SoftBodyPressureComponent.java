package dev.screwbox.playground;

import dev.screwbox.core.environment.Component;

public class SoftBodyPressureComponent implements Component {

    public double pressure;

    public SoftBodyPressureComponent(final double pressure) {
        this.pressure = pressure;
    }
}
