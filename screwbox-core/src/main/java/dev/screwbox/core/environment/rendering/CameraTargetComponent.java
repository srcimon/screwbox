package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.graphics.Camera;
import dev.screwbox.core.graphics.Viewport;

import java.io.Serial;

/**
 * Will move the {@link Camera} automatically towards the {@link Entity#position()}.
 */
public class CameraTargetComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Maximum speed of the {@link Camera} motion.
     */
    public double maxSpeed;

    /**
     * Offset between {@link Entity#position()} and target position of the {@link Camera}.
     */
    public Vector offset = Vector.zero();

    /**
     * Allows {@link Camera} to teleport to target when too far away.
     */
    public boolean allowTeleport = true;

    /**
     * Id of the {@link Viewport} that is affected.
     */
    public int viewportId;

    /**
     * Creates a new instance.
     */
    public CameraTargetComponent() {
        this(2);
    }

    /**
     * Creates a new instance with specified {@link #maxSpeed}.
     */
    public CameraTargetComponent(final double maxSpeed) {
        this(0, maxSpeed);
    }

    /**
     * Creates a new instance with specified {@link #viewportId} and {@link #maxSpeed}.
     */
    public CameraTargetComponent(final int viewportId, final double maxSpeed) {
        this.maxSpeed = maxSpeed;
        this.viewportId = viewportId;
    }
}
