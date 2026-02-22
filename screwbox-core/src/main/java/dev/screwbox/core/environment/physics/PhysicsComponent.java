package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

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
     * Reduces {@link #velocity} over time relative to {@link #velocity}. Is independent from friction that is applied
     * when colliding with collider.
     */
    public double friction = 0;

    /**
     * Limits the {@link #velocity} that is applied on motion to a certain amount. {@link Entity} will not move faster
     * than this maximum.
     *
     * @since 3.13.0
     */
    public double maxVelocity = 1000;

    public PhysicsComponent() {
        this(Vector.zero());
    }

    public PhysicsComponent(final Vector velocity) {
        this.velocity = velocity;
    }

}
