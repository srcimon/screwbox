package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;

public class AccelerationComponent implements Component {

    public double maxSpeed;
    public Vector acceleration;

    public AccelerationComponent(final double maxSpeed) {
        this.maxSpeed = maxSpeed;
        this.acceleration = Vector.zero();
    }
}
