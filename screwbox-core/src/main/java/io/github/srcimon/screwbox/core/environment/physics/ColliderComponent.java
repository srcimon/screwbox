package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Percent;
import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class ColliderComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double friction;
    public Percent bounce;
    public boolean isOneWay;

    public ColliderComponent() {
        this(0);
    }

    public ColliderComponent(final double friction) {
        this(friction, Percent.zero());
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
