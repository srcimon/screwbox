package dev.screwbox.core.graphics.smoke;

import dev.screwbox.core.Bounds;
import dev.screwbox.core.Vector;
import dev.screwbox.core.environment.smoke.SmokeRenderSystem;
import dev.screwbox.core.graphics.Color;
import dev.screwbox.core.graphics.Viewport;

/**
 * Add real-time, interactive smoke effects using fluid dynamics.
 *
 * @since 3.33.0
 */
public interface Smoke {

    /**
     * Customize smoke style and behavior.
     */
    Smoke setOptions(SmokeOptions options);

    /**
     * Returns the current smoke options used to customize smoke style and behavior.
     */
    SmokeOptions options();

    /**
     * Emits smoke using the specified values. Also pushes the smoke using the specified velocity.
     *
     * @see #emit(Vector, Vector, double, Color)
     * @see #push(Vector, Vector)
     */
    Smoke emit(Vector position, Vector velocity, double amount, Color color);

    /**
     * Emits smoke using the specified values.
     *
     * @see #emit(Vector, Vector, double, Color)
     */
    Smoke emit(Vector position, double amount, Color color);

    /**
     * Pushes the smoke at the specified position in the specified direction.
     */
    Smoke push(Vector position, Vector velocity);

    /**
     * Pushes the smoke at the specified {@link Bounds} in the specified direction.
     */
    Smoke push(Bounds bounds, Vector velocity);

    /**
     *ASmoothly adjusts the current velocity toward the target velocity within the specified limits.
     */
    Smoke approachTargetVelocity(Bounds bounds, Vector velocity, double adjustmentSpeed);

    /**
     * Renders smoke on all {@link Viewport viewports}. Can be automated using the {@link SmokeRenderSystem}.
     */
    Smoke render(double delta);

    /**
     * Adds an obstacle that resists smoke at the specified bounds.
     */
    Smoke addObstacle(Bounds bounds);
}
