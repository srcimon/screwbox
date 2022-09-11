package de.suzufa.screwbox.core.entities.components;

import de.suzufa.screwbox.core.Percentage;
import de.suzufa.screwbox.core.entities.Component;

public class ColliderComponent implements Component {

    private static final long serialVersionUID = 1L;

    public double friction;
    public Percentage bounce;
    public boolean isOneWay;

    public ColliderComponent() {
        this(0);
    }

    public ColliderComponent(final double friction) {
        this(friction, Percentage.min());
    }

    public ColliderComponent(final double friction, final Percentage bounce) {
        this(friction, bounce, false);
    }

    public ColliderComponent(final double friction, final Percentage bounce, final boolean isOneWay) {
        this.friction = friction;
        this.bounce = bounce;
        this.isOneWay = isOneWay;
    }

}
