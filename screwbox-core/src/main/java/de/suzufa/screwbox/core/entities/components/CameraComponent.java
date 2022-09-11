package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.entities.Component;

public final class CameraComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double zoom;

    public CameraComponent(final double zoom) {
        this.zoom = zoom;
    }

}
