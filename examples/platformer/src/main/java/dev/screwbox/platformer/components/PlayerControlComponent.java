package dev.screwbox.platformer.components;

import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class PlayerControlComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public boolean allowJumpPush = false;
    public boolean jumpDownPressed = false;
    public boolean jumpPressed = false;
    public boolean digPressed = false;
    public boolean leftPressed = false;
    public boolean rightPressed = false;

}
