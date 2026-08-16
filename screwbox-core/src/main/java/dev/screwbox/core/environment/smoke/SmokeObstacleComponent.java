package dev.screwbox.core.environment.smoke;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.core.CanBeBaked;
import dev.screwbox.core.graphics.smoke.Smoke;

import java.io.Serial;

/**
 * Blocks smoke on the {@link Entity#bounds()}.
 *
 * @see Smoke
 * @since 3.33.0
 */
@CanBeBaked
public class SmokeObstacleComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;
}
