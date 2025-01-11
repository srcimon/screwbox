package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.environment.Component;

//TODO javadoc
//TODO changelog
//TODO test
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
