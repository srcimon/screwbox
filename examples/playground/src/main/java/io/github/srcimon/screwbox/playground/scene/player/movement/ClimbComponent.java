package io.github.srcimon.screwbox.playground.scene.player.movement;

import io.github.srcimon.screwbox.core.environment.Component;

public class ClimbComponent implements Component {

    public Enum<?> keyUp;
    public Enum<?> keyDown;
    public double speed;
    public boolean isEnabled = false;

}
