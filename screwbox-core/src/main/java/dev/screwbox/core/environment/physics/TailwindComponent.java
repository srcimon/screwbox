package dev.screwbox.core.environment.physics;

import dev.screwbox.core.Percent;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;

import java.io.Serial;

/**
 * Apply acceleration on nearby physics entities that contain {@link TailwindPropelledComponent}.
 *
 * @since 3.15.0
 */
public class TailwindComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Range of the effect.
     */
    public double range;

    /**
     * Fraction of motion that is added to nearby entities.
     */
    public Percent modifier;

    /**
     * Position of last update. Will be automatically updated by {@link TailwindSystem}.
     */
    public Vector lastPosition;

    public TailwindComponent(final double range) {
        this(range, Percent.half());
    }

    public TailwindComponent(final double range, final Percent modifier) {
        this.range = range;
        this.modifier = modifier;
    }
}
