package io.github.srcimon.screwbox.playground.scene.player.movement;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.logic.EntityState;
import io.github.srcimon.screwbox.core.keyboard.DefaultKey;

public class JumpControlComponent implements Component {

    public Enum<?> key;
    public double acceleration;
    @Deprecated
    public EntityState jumpState;
    public boolean isEnabled = true;
}
