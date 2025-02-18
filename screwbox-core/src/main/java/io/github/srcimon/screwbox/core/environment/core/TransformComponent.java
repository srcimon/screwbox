package io.github.srcimon.screwbox.core.environment.core;

import io.github.srcimon.screwbox.core.Bounds;
import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Specifies size and position of the {@link Entity} within the game world.
 */
public final class TransformComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Bounds bounds;

    /**
     * Creates a new instance with minimal size at {@link Vector#zero()}.
     */
    public TransformComponent() {
       this(Vector.zero());
    }

    /**
     * Creates a new instance using the specified {@link Bounds}.
     */
    public TransformComponent(final Bounds bounds) {
        this.bounds = bounds;
    }

    /**
     * Creates a new instance using minimal size at the specified position.
     */
    public TransformComponent(final Vector position) {
        this(position, 1, 1);
    }

    /**
     * Creates a new instance using specified position, width and height.
     */
    public TransformComponent(final Vector position, final double width, final double height) {
        this(Bounds.atPosition(position, width, height));
    }

    /**
     * Creates a new instance using specified x- and y-position, width and height.
     */
    public TransformComponent(final double x, final double y, final double width, final double height) {
        this(Bounds.atPosition(x, y, width, height));
    }
}
