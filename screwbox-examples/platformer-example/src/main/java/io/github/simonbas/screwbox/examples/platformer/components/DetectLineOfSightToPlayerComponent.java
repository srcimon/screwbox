package io.github.simonbas.screwbox.examples.platformer.components;

import io.github.simonbas.screwbox.core.entities.Component;

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
