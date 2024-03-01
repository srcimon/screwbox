package io.github.srcimon.screwbox.core.environment.camera;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class CameraConfigurationComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Bounds visibleArea;

    public CameraConfigurationComponent(final Bounds visibleArea) {
        this.visibleArea = visibleArea;
    }
}
