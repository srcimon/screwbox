package io.github.srcimon.screwbox.core.environment.physics;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Will apply waves when on fluids when in contact.
 *
 * @since 2.19.0
 */
public class FluidInteractionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public final double threshold;
    public double xModifier;
    public double yModifier;

    public FluidInteractionComponent() {
        this(4, 20);
    }

    public FluidInteractionComponent(final double yModifier, final double threshold) {
        this.yModifier = yModifier;
        this.xModifier = yModifier / 2.0;
        this.threshold = threshold;
    }
}
