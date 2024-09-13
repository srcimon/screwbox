package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

/**
 * Moves the entity towards the specified postion. Does not avoid obstacles. Use {@link MovementPathComponent} instead
 * when more intelligent wayfinding is needed.
 */
public class MovementTargetComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector position;
    public double maxSpeed;
    public double acceleration;

    public MovementTargetComponent(final Vector position) {
        this(position, 100);
    }

    public MovementTargetComponent(final Vector position, final double maxSpeed) {
        this.position = position;
        this.maxSpeed = maxSpeed;
        this.acceleration = maxSpeed * 10;
    }
}
