package io.github.srcimon.screwbox.vacuum.player;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.environment.Component;

public class DashingComponent implements Component {

    public final double force;
    public final Duration duration;
    public final Time started;

    public DashingComponent(final double force, final Duration duration) {
        this.force = force;
        this.duration = duration;
        this.started = Time.now();
    }
}
