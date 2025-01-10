package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.environment.Component;

public class AirFrictionComponent implements Component {

    public double friction;

    public AirFrictionComponent(double friction) {
        this.friction = friction;
    }
}
