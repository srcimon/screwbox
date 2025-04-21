package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Line;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

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
     * Horizontal friction applied by fluid.
     */
    public final double horizontalFriction;

    /**
     * Vertical friction applied by fluid.
     */
    public final double verticalFriction;

    /**
     * Drift up applied by water.
     */
    public final double buoyancy;

    /**
     * Line between to current wave nodes. Is not null when not floating.
     * Will be {@code null} when not floating.
     */
    public Line attachedWave;

    /**
     * Current depth in water. Will be automatically updated by {@link FloatSystem}.
     */
    public double depth;

    public FloatComponent() {
        this(300, 400);
    }

    public FloatComponent(final double friction, final double buoyancy) {
        this.horizontalFriction = friction / 2.0;
        this.verticalFriction = friction;
        this.buoyancy = buoyancy;
    }


}
