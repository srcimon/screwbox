package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Entities with {@link BoidComponent} will avoid the {@link Bounds} of an {@link Entity} containing this component.
 * Boids are not forced out or in theses obstacles. They are only trying. Configure {@link BoidComponent#obstacleAvoidanceStrength}
 * and {@link BoidComponent#obstaclePerceptionRadius} to optimize boid behaviour.
 *
 * @since 3.27.0
 */
public class BoidObstacleComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Keep boids within ({@code true}) or keep them out ({@code false}).
     */
    public boolean isContainer = false;
}
