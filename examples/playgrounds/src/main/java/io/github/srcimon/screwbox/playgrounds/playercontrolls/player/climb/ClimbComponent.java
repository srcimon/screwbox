package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.climb;

import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

public class ClimbComponent implements Component {

    public boolean isGrabbed = false;
    public Time grabStarted = Time.unset();
    public Time grabLost = Time.unset();

}
