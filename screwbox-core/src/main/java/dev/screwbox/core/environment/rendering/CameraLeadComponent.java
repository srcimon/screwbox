package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Sets the {@link CameraTargetComponent#offset} according to {@link Entity} motion to improve visiblity infront of {@link Entity}.
 *
 * @since 3.32.0
 */
public class CameraLeadComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;
}
