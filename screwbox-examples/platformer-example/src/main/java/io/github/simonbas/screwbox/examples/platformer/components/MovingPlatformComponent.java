package io.github.simonbas.screwbox.examples.platformer.components;

import io.github.simonbas.screwbox.core.Vector;
import io.github.simonbas.screwbox.core.entities.Component;

public class MovingPlatformComponent implements Component {

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
