package io.github.srcimon.screwbox.core.environment.ai;

import io.github.srcimon.screwbox.core.environment.Component;
import io.github.srcimon.screwbox.core.environment.Entity;
import io.github.srcimon.screwbox.core.environment.rendering.RenderComponent;

import java.io.Serial;

/**
 * Rotates the sprites of the {@link RenderComponent} towards the
 * specified target {@link Entity}. Does nothing if target {@link Entity} is not found.
 *
 * @since 2.14.0
 */
public class LookAtTargetComponent implements Component {

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

    public LookAtTargetComponent(final int targetId) {
        this.targetId = targetId;
    }
}
