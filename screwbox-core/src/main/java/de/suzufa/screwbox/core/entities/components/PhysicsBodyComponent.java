package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entities.Component;

public class PhysicsBodyComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Vector momentum;
    public boolean ignoreOneWayCollisions;
    public boolean ignoreCollisions;
    public double gravityModifier = 1;
    public double magnetModifier = 1;

    public PhysicsBodyComponent() {
        this(Vector.zero());
    }

    public PhysicsBodyComponent(final Vector momentum) {
        this.momentum = momentum;
    }

}
