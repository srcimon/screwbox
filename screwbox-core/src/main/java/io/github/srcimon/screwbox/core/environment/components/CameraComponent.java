package io.github.srcimon.screwbox.core.environment.components;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public final class CameraComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double zoom;

    public CameraComponent(final double zoom) {
        this.zoom = zoom;
    }

}
