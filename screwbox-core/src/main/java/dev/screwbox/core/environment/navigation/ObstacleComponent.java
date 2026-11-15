package dev.screwbox.core.environment.navigation;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.Bakeable;

import java.io.Serial;

/**
 * Marks the {@link Entity#bounds()} as obstacle that will be avoided in pathfinding.
 *
 * @see NavigationSystem
 */
@Bakeable
public class ObstacleComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

}
