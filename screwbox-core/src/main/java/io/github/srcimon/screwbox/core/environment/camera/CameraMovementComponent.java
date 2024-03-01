package io.github.srcimon.screwbox.core.environment.camera;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

//TODO replace with CameraTargetComponent
public final class CameraMovementComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public int trackedEntityId;
    public double speed;
    public Vector shift = Vector.zero();

    public CameraMovementComponent(final double speed, final int trackedEntityId) {
        this.speed = speed;
        this.trackedEntityId = trackedEntityId;
    }

}
