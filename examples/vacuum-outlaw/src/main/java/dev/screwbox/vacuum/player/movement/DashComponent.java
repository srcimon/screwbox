package dev.screwbox.vacuum.player.movement;

import dev.screwbox.core.Duration;
import dev.screwbox.core.Time;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;

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
