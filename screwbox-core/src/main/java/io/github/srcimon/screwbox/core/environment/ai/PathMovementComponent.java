package io.github.srcimon.screwbox.core.environment.ai;

import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Moves an {@link Entity} along a {@link Path}.
 */
public class PathMovementComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double speed;
    public double acceleration;
    public Path path;

    public PathMovementComponent(final double speed, final double acceleration) {
        this.speed = speed;
        this.acceleration = acceleration;
    }
}