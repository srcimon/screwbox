package io.github.simonbas.screwbox.core.entities.components;

import io.github.simonbas.screwbox.core.Path;
import io.github.simonbas.screwbox.core.entities.Component;

public class AutomovementComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double speed;
    public Path path;

    public AutomovementComponent(final double speed) {
        this.speed = speed;
    }
}