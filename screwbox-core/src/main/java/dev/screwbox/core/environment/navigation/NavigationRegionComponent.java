package dev.screwbox.core.environment.navigation;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.utils.Scheduler;

import java.io.Serial;

//TODO rename in docs
public class NavigationRegionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final int gridSize;
    public final Scheduler updateScheduler;

    public NavigationRegionComponent(final int gridSize, final Scheduler updateScheduler) {
        this.gridSize = gridSize;
        this.updateScheduler = updateScheduler;
    }
}
