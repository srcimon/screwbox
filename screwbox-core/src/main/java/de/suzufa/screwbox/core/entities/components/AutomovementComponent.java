package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.Path;
import de.suzufa.screwbox.core.entities.Component;

public class AutomovementComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double speed;
    public Path path;

    public AutomovementComponent(final double speed) {
        this.speed = speed;
    }
}