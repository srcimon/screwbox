package io.github.srcimon.screwbox.core.environment.controls;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

public class JumpControlComponent implements Component {

    //TODO key constructor

    public Enum<?> key;
    public double acceleration;
    public boolean isEnabled = true;
    public Time lastActivation = Time.unset();
    //TODO how to enforce ground contact timeout with jump control / extra component or timeout or what?
}
