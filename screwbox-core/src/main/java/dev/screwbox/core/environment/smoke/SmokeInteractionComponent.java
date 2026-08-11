package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.smoke.Smoke;

import java.io.Serial;

/**
 * Lets physics entities interact with {@link Smoke}.
 *
 * @see Smoke
 * @since 3.33.0
 */
public class SmokeInteractionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public double modifier = 50;

    /**
     * Creates a new instance.
     */
    public SmokeInteractionComponent() {

    }

    /**
     * Creates a new instance with specified modifier.
     */
    public SmokeInteractionComponent(final double modifier) {
        this.modifier = modifier;
    }
}
