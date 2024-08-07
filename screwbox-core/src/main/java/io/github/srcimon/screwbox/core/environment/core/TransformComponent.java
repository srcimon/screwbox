package io.github.srcimon.screwbox.core.environment.core;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public final class TransformComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Bounds bounds;

    public TransformComponent() {
       this(Vector.zero());
    }

    public TransformComponent(final Bounds bounds) {
        this.bounds = bounds;
    }

    public TransformComponent(final Vector position) {
        this(position, 1, 1);
    }

    public TransformComponent(final Vector position, double width, double height) {
        this(Bounds.atPosition(position, width, height));
    }

    public TransformComponent(final double x, final double y, double width, double height) {
        this(Bounds.atPosition(x, y, width, height));
    }
}
