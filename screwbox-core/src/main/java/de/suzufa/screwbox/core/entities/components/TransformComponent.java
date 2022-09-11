package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.entities.Component;

public final class TransformComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Bounds bounds;

    public TransformComponent(final Bounds bounds) {
        this.bounds = bounds;
    }

}
