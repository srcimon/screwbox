package de.suzufa.screwbox.playground.debo.components;

import de.suzufa.screwbox.core.entities.Component;

public class PlayerControlComponent implements Component {

    private static final long serialVersionUID = 1L;

    public boolean allowJumpPush = false;
    public boolean jumpDownPressed = false;
    public boolean jumpPressed = false;
    public boolean digPressed = false;
    public boolean leftPressed = false;
    public boolean rightPressed = false;

}
