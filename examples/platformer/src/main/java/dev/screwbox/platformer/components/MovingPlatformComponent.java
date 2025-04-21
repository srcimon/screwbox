package dev.screwbox.platformer.components;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class MovingPlatformComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double speed;
    public int waypoint;
    public Vector targetPosition;

    public MovingPlatformComponent(final int waypoint, final double speed) {
        this.waypoint = waypoint;
        this.speed = speed;
    }

    public MovingPlatformComponent(final Vector targetPosition, final double speed) {
        this.targetPosition = targetPosition;
        this.speed = speed;
    }

}
