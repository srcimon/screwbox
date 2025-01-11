package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.environment.Component;

public class AirFrictionComponent implements Component {

    public double frictionX;
    public double frictionY;

    public AirFrictionComponent(double friction) {
        this(friction, friction);
    }

    public AirFrictionComponent(double frictionX, double frictionY) {
        this.frictionX = frictionX;
        this.frictionY = frictionY;
    }
}
