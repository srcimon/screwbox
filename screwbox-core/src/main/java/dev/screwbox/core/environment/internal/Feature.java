package dev.screwbox.core.environment.internal;

import dev.screwbox.core.environment.EntitySystem;
import dev.screwbox.core.environment.ai.PathMovementSystem;
import dev.screwbox.core.environment.ai.PatrolMovementSystem;
import dev.screwbox.core.environment.ai.TargetLockSystem;
import dev.screwbox.core.environment.ai.TargetMovementSystem;
import dev.screwbox.core.environment.audio.SoundSystem;
import dev.screwbox.core.environment.controls.JumpControlSystem;
import dev.screwbox.core.environment.controls.LeftRightControlSystem;
import dev.screwbox.core.environment.controls.SuspendJumpControlSystem;
import dev.screwbox.core.environment.light.LightRenderSystem;
import dev.screwbox.core.environment.light.OptimizeLightPerformanceSystem;
import dev.screwbox.core.environment.logic.AreaTriggerSystem;
import dev.screwbox.core.environment.logic.StateSystem;
import dev.screwbox.core.environment.particles.ParticleBurstSystem;
import dev.screwbox.core.environment.particles.ParticleEmitterSystem;
import dev.screwbox.core.environment.particles.ParticleInteractionSystem;
import dev.screwbox.core.environment.physics.*;
import dev.screwbox.core.environment.rendering.*;
import io.github.srcimon.screwbox.core.environment.physics.*;
import io.github.srcimon.screwbox.core.environment.rendering.*;
import dev.screwbox.core.environment.tweening.TweenDestroySystem;
import dev.screwbox.core.environment.tweening.TweenLightSystem;
import dev.screwbox.core.environment.tweening.TweenOpacitySystem;
import dev.screwbox.core.environment.tweening.TweenPositionSystem;
import dev.screwbox.core.environment.tweening.TweenScaleSystem;
import dev.screwbox.core.environment.tweening.TweenShaderSystem;
import dev.screwbox.core.environment.tweening.TweenSpinSystem;
import dev.screwbox.core.environment.tweening.TweenSystem;

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
            new TweenShaderSystem(),
            new TweenScaleSystem(),
            new TweenOpacitySystem(),
            new TweenSpinSystem(),
            new TweenLightSystem()
    ),

    RENDERING(
            new RenderUiSystem(),
            new RenderNotificationsSystem(),
            new RenderSceneTransitionSystem(),
            new ForegroundRenderSystem(),
            new MovementRotationSystem(),
            new FixedRotationSystem(),
            new FluidRenderSystem(),
            new FloatRotationSystem(),
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
            new FluidSystem(),
            new FloatSystem(),
            new FluidInteractionSystem(),
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
