package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.entities.Component;

public class MagnetComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double force;
    public double range;

    public MagnetComponent(final double force, final double range) {
        this.force = force;
        this.range = range;
    }

}
