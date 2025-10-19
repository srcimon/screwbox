package dev.screwbox.core.environment.navigation;

import dev.screwbox.core.Duration;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.navigation.Navigation;
import dev.screwbox.core.utils.Scheduler;

import java.io.Serial;

/**
 * Will mark the {@link Entity#bounds()} as {@link Navigation#navigationRegion()}. Within the bounds of the navigation
 * region it is possible to use pathfinding.
 */
public class NavigationRegionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Scheduler updateInterval;

    /**
     * Creates a new instance using the default {@link #updateInterval}.
     */
    public NavigationRegionComponent() {
        this(Scheduler.withInterval(Duration.ofMillis(200)));
    }

    /**
     * Creates a new instance using a custom {@link #updateInterval}.
     */
    public NavigationRegionComponent(final Scheduler updateInterval) {
        this.updateInterval = updateInterval;
    }
}