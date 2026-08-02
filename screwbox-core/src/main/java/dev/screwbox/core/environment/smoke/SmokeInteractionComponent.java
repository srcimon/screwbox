package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.graphics.smoke.Smoke;

import java.io.Serial;
//TODO document test
/**
 * Lets physics entities interact with {@link Smoke}.
 *
 * @see Smoke
 * @since 3.33.0
 */
public class SmokeInteractionComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    public Vector speed;

    public SmokeInteractionComponent() {

    }

    public SmokeInteractionComponent(Vector speed) {
        this.speed = speed;
    }
}
