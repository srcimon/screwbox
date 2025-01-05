package io.github.srcimon.screwbox.playgrounds.playercontrolls.player.move;

import io.github.srcimon.screwbox.core.environment.Component;

public class MovementComponent implements Component {

    public final double speed;

    public MovementComponent() {
        this(140);
    }

    public MovementComponent(double speed) {
        this.speed = speed;
    }
}
