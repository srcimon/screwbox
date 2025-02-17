package io.github.srcimon.screwbox.core.environment.internal;

import io.github.srcimon.screwbox.core.environment.EntitySystem;
import io.github.srcimon.screwbox.core.environment.ai.PathMovementSystem;
import io.github.srcimon.screwbox.core.environment.ai.PatrolMovementSystem;
import io.github.srcimon.screwbox.core.environment.ai.TargetLockSystem;
import io.github.srcimon.screwbox.core.environment.ai.TargetMovementSystem;
import io.github.srcimon.screwbox.core.environment.audio.SoundSystem;
import io.github.srcimon.screwbox.core.environment.controls.JumpControlSystem;
import io.github.srcimon.screwbox.core.environment.controls.LeftRightControlSystem;
import io.github.srcimon.screwbox.core.environment.controls.SuspendJumpControlSystem;
import io.github.srcimon.screwbox.core.environment.light.LightRenderSystem;
import io.github.srcimon.screwbox.core.environment.light.OptimizeLightPerformanceSystem;
import io.github.srcimon.screwbox.core.environment.logic.AreaTriggerSystem;
import io.github.srcimon.screwbox.core.environment.logic.StateSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleBurstSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleEmitterSystem;
import io.github.srcimon.screwbox.core.environment.particles.ParticleInteractionSystem;
import io.github.srcimon.screwbox.core.environment.physics.*;
import io.github.srcimon.screwbox.core.environment.rendering.*;
import io.github.srcimon.screwbox.core.environment.tweening.TweenDestroySystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenLightSystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenOpacitySystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenPositionSystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenScaleSystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenSpinSystem;
import io.github.srcimon.screwbox.core.environment.tweening.TweenSystem;

import java.util.List;

public enum Feature {

    AI(
            new PatrolMovementSystem(),
            new PathMovementSystem(),
            new TargetLockSystem(),
            new TargetMovementSystem()
    ),

    CONTROLS(
            new LeftRightControlSystem(),
            new JumpControlSystem(),
            new SuspendJumpControlSystem()
    ),

    TWEENING(
            new TweenSystem(),
            new TweenPositionSystem(),
            new TweenDestroySystem(),
            new TweenScaleSystem(),
            new TweenOpacitySystem(),
            new TweenSpinSystem(),
            new TweenLightSystem()
    ),

    RENDERING(
            new RenderUiSystem(),
            new RenderNotificationsSystem(),
            new RenderSceneTransitionSystem(),
            new RenderOverLightSystem(),
            new MovementRotationSystem(),
            new FixedRotationSystem(),
            new FlipSpriteSystem(),
            new FixedSpinSystem(),
            new RenderSystem(),
            new CameraSystem()
    ),

    LOGIC(
            new AreaTriggerSystem(),
            new StateSystem()
    ),

    PHYSICS(
            new FrictionSystem(),
            new CollisionSensorSystem(),
            new AttachmentSystem(),
            new CollisionDetailsSystem(),
            new AirFrictionSystem(),
            new GravitySystem(),
            new CursorAttachmentSystem(),
            new MagnetSystem(),
            new OptimizePhysicsPerformanceSystem(),
            new PhysicsSystem(),
            new ChaoticMovementSystem(),
            new PhysicsGridUpdateSystem()
    ),

    AUDIO(
            new SoundSystem()
    ),

    LIGHT(
            new LightRenderSystem(),
            new OptimizeLightPerformanceSystem()
    ),

    PARTICLES(
            new ParticleEmitterSystem(),
            new ParticleInteractionSystem(),
            new ParticleBurstSystem()
    );

    final List<EntitySystem> systems;

    Feature(final EntitySystem... systems) {
        this.systems = List.of(systems);
    }
}
