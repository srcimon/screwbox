package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Entities with {@link BoidComponent} will avoid the {@link Bounds} of an {@link Entity} containing this component.
 *
 * @since 3.27.0
 */
public class BoidObstacleComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    //TODO document
    public boolean isContainer = false;
}
