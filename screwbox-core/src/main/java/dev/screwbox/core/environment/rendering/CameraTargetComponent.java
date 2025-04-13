package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class CameraTargetComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double followSpeed;
    public Vector shift = Vector.zero();
    public boolean allowJumping = true;
    public int viewportId;

    public CameraTargetComponent() {
        this(2);
    }
    public CameraTargetComponent(final double followSpeed) {
       this(0, followSpeed);
    }

    public CameraTargetComponent(final int viewportId, final double followSpeed) {
        this.followSpeed = followSpeed;
        this.viewportId = viewportId;
    }
}
