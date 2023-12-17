package io.github.srcimon.screwbox.core.environment.setup;

import io.github.srcimon.screwbox.core.environment.Environment;
import io.github.srcimon.screwbox.core.environment.light.LightRenderSystem;
import io.github.srcimon.screwbox.core.environment.light.OptimizeLightPerformanceSystem;
import io.github.srcimon.screwbox.core.environment.physics.*;
import io.github.srcimon.screwbox.core.environment.rendering.FlipSpriteSystem;
import io.github.srcimon.screwbox.core.environment.rendering.ReflectionRenderSystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderSystem;
import io.github.srcimon.screwbox.core.environment.rendering.RotateSpriteSystem;
import io.github.srcimon.screwbox.core.environment.rendering.ScreenTransitionSystem;
import io.github.srcimon.screwbox.core.graphics.Sprite;

/**
 * The {@link EnvironmentSetup} provides a simple way to setup routine features in the {@link Environment}.
 */
public interface EnvironmentSetup {

    //TODO: enableTweening()

    //TODO: enableLogic()

    /**
     * Adds systems needed for rendering {@link Sprite}s.
     *
     * @see ReflectionRenderSystem
     * @see RotateSpriteSystem
     * @see FlipSpriteSystem
     * @see ScreenTransitionSystem
     * @see RenderSystem
     */
    EnvironmentSetup enableRendering();

    /**
     * Adds all systems needed for physics support in this {@link Environment}.
     *
     * @see AutomovementSystem
     * @see CollisionDetectionSystem
     * @see GravitySystem
     * @see MagnetSystem
     * @see OptimizePhysicsPerformanceSystem
     * @see PhysicsSystem
     */
    EnvironmentSetup enablePhysics();

    /**
     * Adds systems for light rendering. Enables light rendering in the {@link Environment}. If your screen stays dark you have to add some light components.
     *
     * @see #disableLight()
     * @see LightRenderSystem
     * @see OptimizeLightPerformanceSystem
     */
    EnvironmentSetup enableLight();

    /**
     * Removes systems for light rendering. Disables light rendering in the {@link Environment}.
     *
     * @see #enableLight()
     * @see LightRenderSystem
     * @see OptimizeLightPerformanceSystem
     */
    EnvironmentSetup disableLight();
}
