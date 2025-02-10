package io.github.srcimon.screwbox.vacuum.enemies;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.utils.Scheduler;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;

public class SpawnPointComponent implements Component {

    public final Scheduler scheduler = Scheduler.withInterval(ofSeconds(5));
    public final int drawOrder;

    public SpawnPointComponent(final int drawOrder) {
        this.drawOrder = drawOrder;
    }
}
