package dev.screwbox.core.environment.physics;

import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;

import java.io.Serial;

public class PhysicsComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector velocity;
    public boolean ignoreOneWayCollisions;
    public boolean ignoreCollisions;
    public double gravityModifier = 1;
    public double magnetModifier = 1;

    /**
     * Specifies the friction constantly applied on {@link #velocity}. Negative values will speedup {@link Entity}
     * instead of slowing it down. Does not affect friction applied when colliding with {@link ColliderComponent}.
     */
    public double friction = 0;

    public PhysicsComponent() {
        this(Vector.zero());
    }

    public PhysicsComponent(final Vector velocity) {
        this.velocity = velocity;
    }

}
