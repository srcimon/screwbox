package io.github.srcimon.screwbox.core.environment.internal;

import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.light.LightRenderSystem;
import io.github.srcimon.screwbox.core.environment.light.OptimizeLightPerformanceSystem;
import io.github.srcimon.screwbox.core.environment.logic.AreaTriggerSystem;
import io.github.srcimon.screwbox.core.environment.logic.StateSystem;
import io.github.srcimon.screwbox.core.environment.physics.AutomovementSystem;
import io.github.srcimon.screwbox.core.environment.physics.ChaoticMovementSystem;
import io.github.srcimon.screwbox.core.environment.physics.CollisionDetectionSystem;
import io.github.srcimon.screwbox.core.environment.physics.GravitySystem;
import io.github.srcimon.screwbox.core.environment.physics.MagnetSystem;
import io.github.srcimon.screwbox.core.environment.physics.OptimizePhysicsPerformanceSystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsGridUpdateSystem;
import io.github.srcimon.screwbox.core.environment.physics.PhysicsSystem;
import io.github.srcimon.screwbox.core.environment.rendering.CameraSystem;
import io.github.srcimon.screwbox.core.environment.rendering.FlipSpriteSystem;
import io.github.srcimon.screwbox.core.environment.rendering.ReflectionRenderSystem;
import io.github.srcimon.screwbox.core.environment.rendering.RenderSystem;
import io.github.srcimon.screwbox.core.environment.rendering.RotateSpriteSystem;
import io.github.srcimon.screwbox.core.environment.rendering.ScreenTransitionSystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroySystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenLightSystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacitySystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenPositionSystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenSystem;

import java.util.List;

public enum Feature {

    TWEENING(
            new TweenSystem(),
            new TweenPositionSystem(),
            new TweenDestroySystem(),
            new TweenOpacitySystem(),
            new TweenLightSystem()
    ),

    RENDERING(
            new ReflectionRenderSystem(),
            new RotateSpriteSystem(),
            new FlipSpriteSystem(),
            new RenderSystem(),
            new ScreenTransitionSystem(),
            new CameraSystem()
    ),

    LOGIC(
            new AreaTriggerSystem(),
            new StateSystem()
    ),

    PHYSICS(
            new AutomovementSystem(),
            new CollisionDetectionSystem(),
            new GravitySystem(),
            new MagnetSystem(),
            new OptimizePhysicsPerformanceSystem(),
            new PhysicsSystem(),
            new ChaoticMovementSystem(),
            new PhysicsGridUpdateSystem()
    ),

    LIGHT(
            new LightRenderSystem(),
            new OptimizeLightPerformanceSystem()
    );

    final List<EntitySystem> systems;

    Feature(final EntitySystem... systems) {
        this.systems = List.of(systems);
    }
}
