package dev.screwbox.core.environment.physics;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Lets floating physics {@link Entity entities} dive into fluids.
 *
 * @see FloatComponent
 * @see <a href="https://screwbox.dev/docs/guides/dynamic-fluids/">Guide: Dynamic fluids</a>
 * @since 3.1.0
 */
public class DiveComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Depth that will be used when no physics entity on top.
     */
    public Double inactiveDepth;

    /**
     * Maximum depth that the entity will dive.
     */
    public double maxDepth;

    /**
     * Returns the current dive state. Will be automatically updated by {@link DiveSystem}.
     */
    public boolean isDiving = false;
    
    /**
     * Create a new instance with infinite depth.
     */
    public DiveComponent() {
        this(Double.MAX_VALUE);
    }

    /**
     * Create a new instance with custom depth.
     */
    public DiveComponent(double maxDepth) {
        this.maxDepth = maxDepth;
    }
}
