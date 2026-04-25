package dev.screwbox.core.environment.fluids;

import dev.screwbox.core.Duration;
import dev.screwbox.core.environment.Component;

import java.io.Serial;

/**
 * Adds an under water post processing effect on the fluid shape.
 *
 * @since 3.28.0
 */
public class FluidPostProcessingComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Size of the tiles used to create the effect. Lower values are slower but have butter quality. Valid range is 4 to 32.
     */
    public int tileSize = 12;

    /**
     * Interval of wave effect.
     */
    public Duration interval = Duration.ofMillis(600);
}
