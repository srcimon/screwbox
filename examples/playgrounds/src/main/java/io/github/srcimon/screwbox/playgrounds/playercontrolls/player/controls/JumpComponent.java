package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.controls;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

public class JumpComponent implements Component {

    public int remainingJumps = 2;
    public Time last = Time.unset();
}
