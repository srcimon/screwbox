package dev.screwbox.core.environment.light;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Marks an {@link Entity} as orthographic wall. This wall will be illuminated by surrounding
 * light sources even if it is casting shadows via {@link OccluderComponent}.
 *
 * @since 2.9.0
 */
public class OrthographicWallComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;
}
