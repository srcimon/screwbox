package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.environment.Component;

public class MovementControlComponent implements Component {

    //TODO add default controls?
    public double acceleration;
    public double maxSpeed;
    public Enum<?> left;
    public Enum<?> right;

}
