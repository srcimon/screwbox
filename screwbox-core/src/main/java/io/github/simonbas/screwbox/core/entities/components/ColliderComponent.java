package io.github.simonbas.screwbox.core.entities.components;

import io.github.simonbas.screwbox.core.Percent;
import io.github.simonbas.screwbox.core.entities.Component;

public class ColliderComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double friction;
    public Percent bounce;
    public boolean isOneWay;

    public ColliderComponent() {
        this(0);
    }

    public ColliderComponent(final double friction) {
        this(friction, Percent.min());
    }

    public ColliderComponent(final double friction, final Percent bounce) {
        this(friction, bounce, false);
    }

    public ColliderComponent(final double friction, final Percent bounce, final boolean isOneWay) {
        this.friction = friction;
        this.bounce = bounce;
        this.isOneWay = isOneWay;
    }

}
