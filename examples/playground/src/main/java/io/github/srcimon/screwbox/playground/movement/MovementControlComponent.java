package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.environment.Component;

public class MovementControlComponent implements Component {

    public boolean isEnabled = true;
    public double acceleration;
    public double maxSpeed;
    public Enum<?> left;
    public Enum<?> right;

}
