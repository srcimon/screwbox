package dev.screwbox.core.environment.core;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;

import java.io.Serial;

/**
 * Marks an {@link Entity} as static. Will be used by {@link Environment#bakeStaticEntities()} to merge all {@link Component components} marked as {@link Bakeable}.
 *
 * @since 3.35.0
 */
public class StaticBoundsComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;
}
