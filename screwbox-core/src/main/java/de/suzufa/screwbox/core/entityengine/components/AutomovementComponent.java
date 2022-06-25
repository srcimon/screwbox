package de.suzufa.screwbox.core.entityengine.components;

import de.suzufa.screwbox.core.Path;
import de.suzufa.screwbox.core.entityengine.Component;

public class AutomovementComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double speed;
    public Path path;

    public AutomovementComponent(final double speed) {
        this.speed = speed;
    }

}
