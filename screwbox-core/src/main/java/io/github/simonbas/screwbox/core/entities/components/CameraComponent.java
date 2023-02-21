package io.github.simonbas.screwbox.core.entities.components;

import io.github.simonbas.screwbox.core.entities.Component;

import java.io.Serial;

public final class CameraComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double zoom;

    public CameraComponent(final double zoom) {
        this.zoom = zoom;
    }

}
