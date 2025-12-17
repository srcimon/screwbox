package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Adds shape matching to a soft body {@link Entity} to keep the body in the original shape.
 *
 * @since 3.18.0
 */
public class SoftBodyShapeComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    //TODO boolean allowRotation = true;
    public Polygon shape;

}
