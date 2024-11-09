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

    public double friction = 0;

    public PhysicsComponent() {
        this(Vector.zero());
    }

    public PhysicsComponent(final Vector momentum) {
        this.momentum = momentum;
    }

}
