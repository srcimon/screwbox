package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class MovementTargetComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector position;
    public double maxSpeed;
    public double acceleration = 800;

    public MovementTargetComponent(final Vector position) {
        this(position, 200);
    }

    public MovementTargetComponent(final Vector position, final double maxSpeed) {
        this.position = position;
        this.maxSpeed = maxSpeed;
    }
}
