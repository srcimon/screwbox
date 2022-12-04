package de.suzufa.screwbox.examples.platformer.components;

import de.suzufa.screwbox.core.entities.Component;

public class DetectLineOfSightToPlayerComponent implements Component {

    private static final long serialVersionUID = 1L;

    public boolean isInLineOfSight = false;
    public final double maxDitance;

    public DetectLineOfSightToPlayerComponent(final double maxDistance) {
        this.maxDitance = maxDistance;
    }
}
