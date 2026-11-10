package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Moves the entity directly towards the specified position or entity. Does not avoid obstacles.
 * Use {@link PathMovementComponent} instead when more intelligent pathfinding is needed.
 */
public class TargetMovementComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Target position for the movement.
     */
    public Vector position;

    /**
     * Maximums speed used for movement.
     */
    public double maxSpeed;

    /**
     * Acceleration towards {@link #maxSpeed} for movement.
     */
    public double acceleration;

    /**
     * When set will constantly update the {@link #position} with the {@link Entity#position()} of the entity with the specified id.
     *
     * @since 3.40.0
     */
    public Integer targetId;

    public TargetMovementComponent(final int targetId) {
        this(targetId, 100);
    }

    public TargetMovementComponent(final Vector position) {
        this(position, 100);
    }

    public TargetMovementComponent(final Vector position, final double maxSpeed) {
        this.position = position;
        this.maxSpeed = maxSpeed;
        this.acceleration = maxSpeed * 10;
    }

    public TargetMovementComponent(final Integer targetId, final double maxSpeed) {
        this.targetId = targetId;
        this.maxSpeed = maxSpeed;
        this.acceleration = maxSpeed * 10;
    }
}
