package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.smoke.Smoke;

import java.io.Serial;
//TODO test
/**
 * Lets physics entities interact with {@link Smoke}.
 *
 * @see Smoke
 * @since 3.33.0
 */
public class SmokeInteractionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Percent modifier = Percent.of(0.3);

    /**
     * Creates a new instance.
     */
    public SmokeInteractionComponent() {

    }

    /**
     * Creates a new instance with specified modifier.
     */
    public SmokeInteractionComponent(final Percent modifier) {
        this.modifier = modifier;
    }
}
