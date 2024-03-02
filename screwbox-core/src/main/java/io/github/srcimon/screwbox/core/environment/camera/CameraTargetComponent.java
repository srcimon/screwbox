package io.github.srcimon.screwbox.core.environment.camera;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class CameraTargetComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double speed;

    public Vector shift = Vector.zero();

    public CameraTargetComponent() {
        this(2);
    }
    public CameraTargetComponent(final double speed) {
        this.speed = speed;
    }
}
