package io.github.srcimon.screwbox.vacuum.enemies;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.utils.Scheduler;

import static dev.screwbox.core.Duration.ofSeconds;

public class SpawnPointComponent implements Component {

    public final Scheduler scheduler = Scheduler.withInterval(ofSeconds(5));
    public final int drawOrder;

    public SpawnPointComponent(final int drawOrder) {
        this.drawOrder = drawOrder;
    }
}
