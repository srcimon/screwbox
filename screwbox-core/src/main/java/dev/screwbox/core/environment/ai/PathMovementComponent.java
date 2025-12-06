package dev.screwbox.core.environment.ai;

import dev.screwbox.core.navigation.Polygon;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Moves an {@link Entity} along a {@link Polygon}.
 */
public class PathMovementComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double speed;
    public double acceleration;
    public Polygon path;

    public PathMovementComponent(final double speed, final double acceleration) {
        this.speed = speed;
        this.acceleration = acceleration;
    }
}