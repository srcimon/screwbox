package io.github.srcimon.screwbox.examples.platformer.components;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class DetectLineOfSightToPlayerComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public boolean isInLineOfSight = false;
    public final double maxDitance;

    public DetectLineOfSightToPlayerComponent(final double maxDistance) {
        this.maxDitance = maxDistance;
    }
}
