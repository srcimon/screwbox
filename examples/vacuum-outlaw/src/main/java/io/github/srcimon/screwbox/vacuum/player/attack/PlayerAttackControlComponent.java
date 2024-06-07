package io.github.srcimon.screwbox.vacuum.player.attack;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

public class PlayerAttackControlComponent implements Component {

    public Duration reloadDuration = Duration.ofMillis(800);
    public Time lastShotFired = Time.unset();
}
