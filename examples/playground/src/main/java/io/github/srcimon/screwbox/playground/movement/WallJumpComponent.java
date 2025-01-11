package io.github.srcimon.screwbox.playground.movement;

import io.github.srcimon.screwbox.core.environment.Component;

public class WallJumpComponent implements Component {

    public boolean isEnabled = false;
    public Enum<?> key;
    public double acceleration;
    public boolean isLeft;
}
