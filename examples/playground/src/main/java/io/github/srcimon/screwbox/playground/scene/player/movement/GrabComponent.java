package io.github.srcimon.screwbox.playground.scene.player.movement;

import io.github.srcimon.screwbox.core.environment.Component;

public class GrabComponent implements Component {

    public boolean isEnabled = false;
    public Enum<?> grabKey;
    public double stamina;
}
