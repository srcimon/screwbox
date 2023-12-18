package io.github.srcimon.screwbox.core.environment;

import io.github.srcimon.screwbox.core.environment.light.LightRenderSystem;
import io.github.srcimon.screwbox.core.environment.light.OptimizeLightPerformanceSystem;
import io.github.srcimon.screwbox.core.environment.logic.AreaTriggerSystem;
import io.github.srcimon.screwbox.core.environment.logic.StateSystem;
import io.github.srcimon.screwbox.core.environment.physics.*;
import io.github.srcimon.screwbox.core.environment.rendering.*;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroySystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacitySystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenSystem;
import io.github.srcimon.screwbox.core.graphics.Sprite;

/**
 * The {@link EnvironmentSetup} provides a simple way to setup routine features in the {@link Environment}.
 */
public class EnvironmentSetup {

    private final Environment environment;

    public EnvironmentSetup(final Environment environment) {
        this.environment = environment;
    }

    /**
     * Adds systems needed for tweening.
     *
     * @see TweenSystem
     * @see TweenDestroySystem
     * @see TweenOpacitySystem
     */
    public EnvironmentSetup enableTweening() {
        environment
                .addOrReplaceSystem(new TweenSystem())
                .addOrReplaceSystem(new TweenDestroySystem())
                .addOrReplaceSystem(new TweenOpacitySystem());
        return this;
    }

    /**
     * Adds systems needed for stateful entities and area triggers.
     *
     * @see AreaTriggerSystem
     * @see StateSystem
     */
    public EnvironmentSetup enableLogic() {
        environment
                .addOrReplaceSystem(new AreaTriggerSystem())
                .addOrReplaceSystem(new StateSystem());
        return this;
    }

    /**
     * Adds systems needed for rendering {@link Sprite}s.
     *
     * @see ReflectionRenderSystem
     * @see RotateSpriteSystem
     * @see FlipSpriteSystem
     * @see ScreenTransitionSystem
     * @see RenderSystem
     */
    public EnvironmentSetup enableRendering() {
        environment
                .addOrReplaceSystem(new ReflectionRenderSystem())
                .addOrReplaceSystem(new RotateSpriteSystem())
                .addOrReplaceSystem(new FlipSpriteSystem())
                .addOrReplaceSystem(new RenderSystem())
                .addOrReplaceSystem(new ScreenTransitionSystem());
        return this;
    }

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
    public EnvironmentSetup enablePhysics() {
        environment
                .addOrReplaceSystem(new AutomovementSystem())
                .addOrReplaceSystem(new CollisionDetectionSystem())
                .addOrReplaceSystem(new GravitySystem())
                .addOrReplaceSystem(new MagnetSystem())
                .addOrReplaceSystem(new OptimizePhysicsPerformanceSystem())
                .addOrReplaceSystem(new PhysicsSystem());
        return this;
    }

    /**
     * Adds systems for light rendering. Enables light rendering in the {@link Environment}. If your screen stays dark you have to add some light components.
     *
     * @see #disableLight()
     * @see LightRenderSystem
     * @see OptimizeLightPerformanceSystem
     */
    public EnvironmentSetup enableLight() {
        environment
                .addOrReplaceSystem(new LightRenderSystem())
                .addOrReplaceSystem(new OptimizeLightPerformanceSystem());
        return this;
    }

    /**
     * Removes systems for light rendering. Disables light rendering in the {@link Environment}.
     *
     * @see #enableLight()
     * @see LightRenderSystem
     * @see OptimizeLightPerformanceSystem
     */
    public EnvironmentSetup disableLight() {
        environment
                .removeSystemIfPresent(LightRenderSystem.class)
                .removeSystemIfPresent(OptimizeLightPerformanceSystem.class);
        return this;
    }
}
