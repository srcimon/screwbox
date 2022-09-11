package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Component;

public final class CameraMovementComponent implements Component {

    private static final long serialVersionUID = 1L;

    public int trackedEntityId;
    public double speed;
    public Vector shift = Vector.zero();

    public CameraMovementComponent(final double speed, final int trackedEntityId) {
        this.speed = speed;
        this.trackedEntityId = trackedEntityId;
    }

}
