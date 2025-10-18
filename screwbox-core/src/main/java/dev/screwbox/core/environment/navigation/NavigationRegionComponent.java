package dev.screwbox.core.environment.navigation;

import dev.screwbox.core.Duration;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.utils.Scheduler;

import java.io.Serial;

public class NavigationRegionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Scheduler updateInterval;

    public NavigationRegionComponent() {
        this(Scheduler.withInterval(Duration.ofMillis(200)));
    }

    public NavigationRegionComponent(Scheduler updateInterval) {
        this.updateInterval = updateInterval;
    }
}