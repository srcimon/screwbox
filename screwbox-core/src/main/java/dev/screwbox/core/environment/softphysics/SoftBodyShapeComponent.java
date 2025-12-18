package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Polygon;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;

import java.io.Serial;

/**
 * Adds shape matching to a soft body {@link Entity} to preserve the body in the original shape during simulation.
 *
 * @since 3.18.0
 */
public class SoftBodyShapeComponent implements Component {
    //TODO add to guide
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The shape the soft body tries to preserve. Will be automatically added using the current {@link SoftBodyComponent#shape}.
     */
    public Polygon shape;

    /**
     * Specify if rotation will be used to preserve the soft body shape. Turning rotation of keeps body upright.
     */
    public boolean isRotationAllowed = true;

    /**
     * Specify a minimum distance between projected node and current node of the soft body to take action. Can be used
     * to prevent ghost motion.
     */
    public double deadZone = 4;

    /**
     * The strength used to keep the soft body nodes within shape.
     */
    public double strength = 20;

    /**
     * The flexibility used to keep the soft body nodes within shape.
     */
    public double flexibility = 20;
}
