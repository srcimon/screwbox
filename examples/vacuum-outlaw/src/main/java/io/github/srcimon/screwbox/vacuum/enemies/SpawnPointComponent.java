package io.github.srcimon.screwbox.vacuum.enemies;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.utils.Sheduler;

import static io.github.srcimon.screwbox.core.Duration.ofSeconds;

public class SpawnPointComponent implements Component {

    final public Sheduler sheduler = Sheduler.withInterval(ofSeconds(5));
    final public int drawOrder;

    public SpawnPointComponent(final int drawOrder) {
        this.drawOrder = drawOrder;
    }
}
