package dev.screwbox.core.environment.navigation;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.utils.Scheduler;

import java.io.Serial;

public class PhysicsGridConfigurationComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final int gridSize;
    public final Bounds worldBounds;
    public final Scheduler updateScheduler;

    public PhysicsGridConfigurationComponent(final Bounds worldBounds, final int gridSize, final Scheduler updateScheduler) {
        this.worldBounds = worldBounds;
        this.gridSize = gridSize;
        this.updateScheduler = updateScheduler;
    }
}
