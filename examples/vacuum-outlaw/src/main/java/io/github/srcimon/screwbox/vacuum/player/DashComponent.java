package io.github.srcimon.screwbox.vacuum.player;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.Time;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;

public class DashComponent implements Component {

    public final Vector force;
    public final Duration duration;
    public final Time started;

    public DashComponent(final Vector force, final Duration duration) {
        this.force = force;
        this.duration = duration;
        this.started = Time.now();
    }
}
