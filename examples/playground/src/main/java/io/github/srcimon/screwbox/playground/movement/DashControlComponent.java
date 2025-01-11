package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.environment.Component;

public class DashControlComponent implements Component {

    public boolean isEnabled;
    public int remainingDashes = 1;
    public Enum<?> dashKey;
    public Enum<?> upKey;
    public Enum<?> leftKey;
    public Enum<?> rightKey;
    public double speed = 200;
}
