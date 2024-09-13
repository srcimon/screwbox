package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class MovementPathComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double speed;
    public double acceleration;
    public Path path;

    public MovementPathComponent(final double speed, final  double acceleration) {
        this.speed = speed;
        this.acceleration = acceleration;
    }
}