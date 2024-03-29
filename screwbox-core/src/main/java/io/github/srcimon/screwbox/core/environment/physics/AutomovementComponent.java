package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class AutomovementComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double speed;
    public Path path;

    public AutomovementComponent(final double speed) {
        this.speed = speed;
    }
}