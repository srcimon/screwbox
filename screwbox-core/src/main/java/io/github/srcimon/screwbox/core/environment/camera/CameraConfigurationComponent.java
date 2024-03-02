package io.github.srcimon.screwbox.core.environment.camera;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public final class CameraConfigurationComponent implements Component {

    //TODO remove camera from Tiled Maps
    @Serial
    private static final long serialVersionUID = 1L;

    public Bounds visibleArea;

    public CameraConfigurationComponent(final Bounds visibleArea) {
        this.visibleArea = visibleArea;
    }

}
