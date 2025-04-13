package io.github.srcimon.screwbox.vacuum.player.attack;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.environment.Component;

public class PlayerAttackControlComponent implements Component {

    public Duration reloadDuration = Duration.ofMillis(800);
    public Time lastShotFired = Time.unset();
}
