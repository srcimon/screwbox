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

    public final double modifier;
    public final double threshold;

    public FluidInteractionComponent() {
        this(4, 50);
    }

    public FluidInteractionComponent(final double modifier, final double threshold) {
        this.modifier = modifier;
        this.threshold = threshold;
    }
}
