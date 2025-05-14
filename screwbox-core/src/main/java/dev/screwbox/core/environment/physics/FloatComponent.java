package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Line;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Lets physics {@link Entity entities} float on fluids.
 *
 * @see <a href="https://screwbox.dev/docs/guides/dynamic-fluids/">Guide: Dynamic fluids</a>
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
    public double buoyancy;

    /**
     * Line between to current wave nodes. Is not null when not floating.
     * Will be {@code null} when not floating.
     */
    public Line attachedWave;

    /**
     * Current depth in fluid. Will be automatically updated by {@link FloatSystem}.
     */
    public double depth;

    /**
     * Dive depth into fluids. Will be multiplied with body height. 0.5 will result in half submerged body.
     *
     * @since 3.1.0
     */
    public double dive = 0.5;

    public FloatComponent() {
        this(300, 340);
    }

    public FloatComponent(final double friction, final double buoyancy) {
        this.horizontalFriction = friction / 2.0;
        this.verticalFriction = friction;
        this.buoyancy = buoyancy;
    }


}
