package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Vector;
import io.github.srcimon.screwbox.core.environment.Component;

public class RigidBodyComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Vector momentum;
    public boolean ignoreOneWayCollisions;
    public boolean ignoreCollisions;
    public double gravityModifier = 1;
    public double magnetModifier = 1;

    public RigidBodyComponent() {
        this(Vector.zero());
    }

    public RigidBodyComponent(final Vector momentum) {
        this.momentum = momentum;
    }

}
