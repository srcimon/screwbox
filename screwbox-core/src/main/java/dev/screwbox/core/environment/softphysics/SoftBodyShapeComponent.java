package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Adds shape matching to a soft body {@link Entity} to keep the body in the original shape during simulation.
 *
 * @since 3.18.0
 */
public class SoftBodyShapeComponent implements Component {
//TODO add to guide
    @Serial
    private static final long serialVersionUID = 1L;

    //TODO document
    public Polygon shape;

    //TODO document
    public boolean isRotationAllowed = true;
}
