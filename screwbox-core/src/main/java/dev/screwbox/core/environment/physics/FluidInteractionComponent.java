package dev.screwbox.core.environment.physics;

import dev.screwbox.core.environment.Component;

import java.io.Serial;

/**
 * Will apply waves when on fluids when in contact.
 *
 * @since 2.19.0
 */
public class FluidInteractionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Speed changes below will be ignored to avoid endless interaction loop when standing still.
     */
    public final double threshold;

    /**
     * Modifier for horizontal impact.
     */
    public double xModifier;

    /**
     * Modifier for vertical impact. Normally double the value of horizontal impact.
     */
    public double yModifier;

    public FluidInteractionComponent() {
        this(2, 30);
    }

    public FluidInteractionComponent(final double yModifier, final double threshold) {
        this.yModifier = yModifier;
        this.xModifier = yModifier / 2.0;
        this.threshold = threshold;
    }
}
