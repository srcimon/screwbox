package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.graphics.ViewportName;

import java.io.Serial;

public class CameraTargetComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double followSpeed;
    public Vector shift = Vector.zero();
    public boolean allowJumping = true;
    public ViewportName viewportName = ViewportName.FIRST;

    public CameraTargetComponent() {
        this(2);
    }
    public CameraTargetComponent(final double followSpeed) {
       this(ViewportName.FIRST, 2);
    }

    public CameraTargetComponent(final ViewportName viewportName, final double followSpeed) {
        this.followSpeed = followSpeed;
        this.viewportName = viewportName;
    }
}
