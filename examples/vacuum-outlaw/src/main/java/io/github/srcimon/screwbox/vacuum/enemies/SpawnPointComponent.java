package io.github.srcimon.screwbox.vacuum.enemies;

import io.github.srcimon.screwbox.core.Duration;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.utils.Sheduler;

public class SpawnPointComponent implements Component {

    final public Sheduler sheduler = Sheduler.withInterval(Duration.ofSeconds(15));
    final public int drawOrder;

    public SpawnPointComponent(final int drawOrder) {
        this.drawOrder = drawOrder;
    }
}
