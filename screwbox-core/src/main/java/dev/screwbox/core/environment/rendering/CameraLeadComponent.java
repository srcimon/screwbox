package dev.screwbox.core.environment.rendering;

import dev.screwbox.core.environment.Component;
import dev.screwbox.core.environment.Entity;
import dev.screwbox.core.environment.physics.PhysicsComponent;

import java.io.Serial;

/**
 * Sets the {@link CameraTargetComponent#offset} according to {@link Entity} motion to improve visiblity infront of {@link Entity}.
 *
 * @since 3.32.0
 */
public class CameraLeadComponent implements Component {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Horizontal impact of {@link PhysicsComponent#velocity} to {@link CameraTargetComponent#offset}.
     */
    public double xModifier = 1.0;

    /**
     * Vertical impact of {@link PhysicsComponent#velocity} to {@link CameraTargetComponent#offset}. It is recommended
     * to set below 1.0 to reduce camera sway in Jump and Run games.
     */
    public double yModifier = 1.0;

    /**
     * Speed of camera adjustment.
     */
    public double adjustmentSpeed = 5;
}
