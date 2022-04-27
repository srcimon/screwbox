package de.suzufa.screwbox.core.entityengine.components;

import de.suzufa.screwbox.core.entityengine.Component;

public final class CameraComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double zoom;

    public CameraComponent(final double zoom) {
        this.zoom = zoom;
    }

}
