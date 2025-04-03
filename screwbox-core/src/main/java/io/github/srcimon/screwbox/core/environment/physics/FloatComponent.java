package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.environment.Component;

import java.io.Serial;

public class FloatComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final double friction;
    public final double buoyancy;

    public FloatComponent() {
        this(400, 400);
    }

    public FloatComponent(final double friction, final  double buoyancy) {
        this.friction = friction;
        this.buoyancy = buoyancy;
    }


}
