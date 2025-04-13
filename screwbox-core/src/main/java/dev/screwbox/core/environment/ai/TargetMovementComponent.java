package dev.screwbox.core.environment.ai;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;

import java.io.Serial;

/**
 * Moves the entity towards the specified position. Does not avoid obstacles. Use {@link PathMovementComponent} instead
 * when more intelligent pathfinding is needed.
 */
public class TargetMovementComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector position;
    public double maxSpeed;
    public double acceleration;

    public TargetMovementComponent(final Vector position) {
        this(position, 100);
    }

    public TargetMovementComponent(final Vector position, final double maxSpeed) {
        this.position = position;
        this.maxSpeed = maxSpeed;
        this.acceleration = maxSpeed * 10;
    }
}
