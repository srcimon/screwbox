package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.Line;
import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Lets physics {@link Entity entities} float on fluids.
 *
 * @since 2.19.0
 */
public class FloatComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Friction applied by fluid.
     */
    public final double friction;

    /**
     * Drift up applied by water.
     */
    public final double buoyancy;

    public Line attachedWave;

    public FloatComponent() {
        this(300, 400);
    }

    public FloatComponent(final double friction, final double buoyancy) {
        this.friction = friction;
        this.buoyancy = buoyancy;
    }


}
