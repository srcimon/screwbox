package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.utils.Scheduler;

import java.io.Serial;

/**
 * Moves an {@link Entity} along a {@link Polygon path}.
 */
public class PathMovementComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Speed of the movement.
     */
    public double speed;

    /**
     * Acceleration of the movement.
     */
    public double acceleration;

    /**
     * When set will update the path regularly to the position of the specified {@link Entity}.
     *
     * @since 3.41.0
     */
    public Integer targetId;

    /**
     * Update timer when following an entity specified by {@link #targetId}.
     *
     * @since 3.41.0
     */
    public Scheduler scheduler = Scheduler.ofMillis(500);

    /**
     * Path that is followed.
     */
    public Polygon path;

    public PathMovementComponent(final double speed, final double acceleration) {
        this.speed = speed;
        this.acceleration = acceleration;
    }
}