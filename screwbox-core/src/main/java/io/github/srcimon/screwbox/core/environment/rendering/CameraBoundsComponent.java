package io.github.srcimon.screwbox.core.environment.rendering;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

/**
 * Sets the bounds in which the camera will be moved when using {@link CameraTargetComponent}.
 */
public final class CameraBoundsComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final Bounds cameraBounds;

    public CameraBoundsComponent(final Bounds cameraBounds) {
        this.cameraBounds = cameraBounds;
    }

}
