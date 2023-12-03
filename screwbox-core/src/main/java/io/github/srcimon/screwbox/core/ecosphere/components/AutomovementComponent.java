package io.github.srcimon.screwbox.core.ecosphere.components;

import io.github.srcimon.screwbox.core.Path;
import io.github.srcimon.screwbox.core.ecosphere.Component;

public class AutomovementComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double speed;
    public Path path;

    public AutomovementComponent(final double speed) {
        this.speed = speed;
    }
}