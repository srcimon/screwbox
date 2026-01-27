package dev.screwbox.core.environment.softphysics;

import dev.screwbox.core.Angle;
import dev.screwbox.core.Percent;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.Environment;

import java.io.Serial;

/**
 * Links one {@link Entity} to another with a flexible link. Used to create ropes and soft bodies.
 *
 * @since 3.16.0
 */
public class SoftLinkComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance with specified {@link #targetId}.
     */
    public SoftLinkComponent(final int targetId) {
        this.targetId = targetId;
    }

    /**
     * Id of the {@link Entity} that will be linked. Entity must be present within {@link Environment}, otherwise
     * {@link SoftPhysicsSystem} will throw an exception.
     */
    public final int targetId;

    /**
     * Angle of the link. Will be automatically updated by {@link SoftPhysicsSystem}.
     */
    public Angle angle = Angle.none();

    /**
     * Rest length of the link. Will be updated to current distance when zero.
     */
    public double length;

    /**
     * Retract strength used for pulling link {@link Entity entities} together.
     */
    public double retract = 30;

    /**
     * Expand strength used for pushing link {@link Entity entities} away from each other.
     */
    public double expand = 30;

    /**
     * Flexibility of the link. Lower values will reduce speed changes applied by the link.
     */
    public double flexibility = 150;

    /**
     * Stiffness of the link.
     *
     * @since 3.21.0
     */
    public Percent stiffness = Percent.of(0.125);
}
