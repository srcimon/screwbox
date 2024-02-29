package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.SingletonComponent;
import io.github.srcimon.screwbox.core.utils.Sheduler;

import java.io.Serial;

//TODO @SingletonComponent on Adding such a component will check for already present ones => PERFORMANCE
public class PhysicsGridConfigurationComponent implements SingletonComponent {

    @Serial
    private static final long serialVersionUID = 1L;

    public final int gridSize;
    public final Bounds worldBounds;
    public final Sheduler updateSheduler;

    public PhysicsGridConfigurationComponent(final Bounds worldBounds, final int gridSize, final Sheduler updateSheduler) {
        this.worldBounds = worldBounds;
        this.gridSize = gridSize;
        this.updateSheduler = updateSheduler;
    }
}
