package io.github.srcimon.screwbox.core.environment.ai;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

//TODO javadoc
//TODO changelog
public class AiPatrolMovementComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double speed;

    public AiPatrolMovementComponent(final double speed) {
        this.speed = speed;
    }
}
