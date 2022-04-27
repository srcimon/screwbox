package de.suzufa.screwbox.core.entityengine.components;

import de.suzufa.screwbox.core.Vector;
import de.suzufa.screwbox.core.entityengine.Component;

public class GravityComponent implements Component {

    private static final long serialVersionUID = 1L;

    public Vector gravity;

    public GravityComponent(Vector gravity) {
        this.gravity = gravity;
    }

}
