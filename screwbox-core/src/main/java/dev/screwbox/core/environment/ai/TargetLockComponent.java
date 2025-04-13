package dev.screwbox.core.environment.ai;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.rendering.RenderComponent;

import java.io.Serial;

/**
 * Rotates the sprites of the {@link RenderComponent} towards the
 * specified target {@link Entity}. Does nothing if target {@link Entity} is not found.
 *
 * @since 2.14.0
 */
public class TargetLockComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The id of the target entity.
     */
    public int targetId;

    /**
     * The speed in rotations per second that is used to rotate towards the target entity.
     */
    public double speed = 10.0;

    public TargetLockComponent(final int targetId) {
        this.targetId = targetId;
    }
}
