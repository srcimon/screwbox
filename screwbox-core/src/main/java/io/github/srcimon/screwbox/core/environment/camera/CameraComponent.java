package io.github.srcimon.screwbox.core.environment.camera;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public final class CameraComponent implements Component {

    //TODO remove camera from Tiled Maps
    @Serial
    private static final long serialVersionUID = 1L;

    public double zoom;
    public Bounds visibleArea;

    public CameraComponent(final double zoom, final Bounds visibleArea) {
        this.zoom = zoom;
        this.visibleArea = visibleArea;
    }

}
