package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.Bounds;
import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Component;

public final class TransformComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Bounds bounds;

    public TransformComponent(final Bounds bounds) {
        this.bounds = bounds;
    }

    public TransformComponent(final Vector position) {
        this(position, 1, 1);
    }

    public TransformComponent(final Vector position, double width, double height) {
        this(Bounds.atPosition(position, width, height));
    }
}
