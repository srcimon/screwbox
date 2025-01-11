package io.github.srcimon.screwbox.playground.scene.player.movement;

import io.github.srcimon.screwbox.core.environment.Component;

public class WallJumpComponent implements Component {

    public boolean isEnabled = false;
    public Enum<?> keyJump;
    public Enum<?> keyLeft;
    public Enum<?> keyRight;
    public double minorAcceleration;
    public boolean isLeft;
    public double strongAcceleration;
}
