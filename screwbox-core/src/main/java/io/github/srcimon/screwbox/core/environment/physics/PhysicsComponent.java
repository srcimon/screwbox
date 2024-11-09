package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class PhysicsComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector momentum;
    public boolean ignoreOneWayCollisions;
    public boolean ignoreCollisions;
    public double gravityModifier = 1;
    public double magnetModifier = 1;

    /**
     * Specifies the friction constantly applied on {@link #momentum}. Negative values will speed
     * up {@link io.github.srcimon.screwbox.core.environment.Entity} instead of slowing it down.
     * Does not affect friction applied when colliding with {@link ColliderComponent}.
     */
    public double friction = 0;

    public PhysicsComponent() {
        this(Vector.zero());
    }

    public PhysicsComponent(final Vector momentum) {
        this.momentum = momentum;
    }

}
