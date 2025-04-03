---
sidebar_position: 2
---

# Components overview

An overview over all components shipped with the [ScrewBox ECS](../fundamentals/ecs.md)
Also have a look at the [standalone systems](standalone-systems).
Adding a `Component` to an `Entity` will automatically add the described behaviour if the corresponding `EntitySystem`
was added to the `Environment`.
See [Prepacked systems and components](../fundamentals/ecs.md#prepacked-systems-and-components) for more information.

:::tip
To use all components out of the box use `engine.enableAllFeatures()`.
But this will come with some unnecessary systems that will consume a neglectable time.
So if you struggle with fps it might be a good idea to avoid unnecessary systems.
Otherwise this is the recommended way to go.
:::

## Common Components

| Component            | Description                                                                             |
|----------------------|-----------------------------------------------------------------------------------------|
| `TransformComponent` | Most common component. Specifies size and position of the entity within the game world. |

## AI components

To use these components call `environment.enableAi()` first.

| Component                 | Description                                                                       |
|---------------------------|-----------------------------------------------------------------------------------|
| `PatrolMovementComponent` | Applies a patrolling movement pattern.                                            |
| `PathMovementComponent`   | Moves an entity along a `Path`.                                                   |
| `TargetMovementComponent` | Moves the entity towards the specified position. Does not avoid obstacles.        |
| `TargetLockComponent`     | Rotates the sprites of the `RenderComponent` towards the specified target entity. |

## Control components

To use these components call `environment.enableControls()` first.

| Component                     | Description                                      |
|-------------------------------|--------------------------------------------------|
| `LeftRightControlComponent`   | Let the entity move left and right on key press. |
| `JumpControlComponent`        | Let the entity do a jump on key press.           |
| `SuspendJumpControlComponent` | Enforce ground contact for jumping.              |

## Tweening components

To use these components call `environment.enableTweening()` first.

| Component                     | Description                                                            |
|-------------------------------|------------------------------------------------------------------------|
| `TweenComponent`              | Adds tweening to an entity. Required for all tween related components. |
| `TweenPositionComponent`      | Adds tweening movement from one position to another.                   |
| `TweenShaderComponent`        | Tweens progress of entity shader.                                      |
| `TweenOrbitPositionComponent` | Adds an orbiting movement around a central position.                   |
| `TweenDestroyComponent`       | Removes the entity after the tween is finished.                        |
| `TweenScaleComponent`         | Tweens the scale of an `Sprite` used in the `RenderComponent`.         |
| `TweenOpacityComponent`       | Tweens the opacity of an `Sprite` used in the `RenderComponent`.       |
| `TweenSpinComponent`          | Tweens the spin `Sprite` used in the `RenderComponent`.                |
| `TweenLightComponent`         | Tweens the opacity of all light related components.                    |

## Rendering components

To use these components call `environment.enableRendering()` first.

| Component                   | Description                                                                           |
|-----------------------------|---------------------------------------------------------------------------------------|
| `RenderComponent`           | Renders a `Sprite` at the entity position.                                            |
| `ReflectionComponent`       | Reflects sprites at the entity bounds. Also supports water animations.                |
| `MovementRotationComponent` | Rotates `Sprites` in direction of movement.                                           |
| `FixedRotationComponent`    | Continuously updates the rendered `Sprite` rotation.                                  |
| `FlipSpriteComponent`       | Flips the `Sprite` horizontally when the entity moves to the left.                    |
| `FixedSpinComponent`        | Continuously updates the rendered `Sprite` spin.                                      |
| `CameraTargetComponent`     | Moves the [camera](../core-modules/camera) towards the entity.                        |
| `CameraBoundsComponent`     | Sets the bounds in which the camera will be moved when using `CameraTargetComponent`. |

## Logic components

To use these components call `environment.enableLogic()` first.

| Component              | Description                                                                         |
|------------------------|-------------------------------------------------------------------------------------|
| `TriggerAreaComponent` | Detects when the `Entity` collides with another matching the specified `Archetype`. |
| `StateComponent`       | Used to add a simple state machine to the entity.                                   |

## Physics components

To use these components call `environment.enablePhysics()` first.

| Component                           | Description                                                                                           |
|-------------------------------------|-------------------------------------------------------------------------------------------------------|
| `PhysicsComponent`                  | Adds physics behaviour to an entity. Applies friction and movement and avoids collisions.             |
| `CollisionSensorComponent`          | Detects collisions with physics.                                                                      |
| `FloatComponent`                    | Lets physics entities float on fluids.                                                                |
| `ColliderComponent`                 | Prevents physic entities from intersecting.                                                           |
| `AttachmentComponent`               | Attaches an entity to another.                                                                        |
| `CollisionDetailsComponent`         | Collects detailed information on collisions detected by `CollisionSensorComponent`.                   |
| `StaticColliderComponent`           | Optimizes performance when added to entities with `ColliderComponent` that will not move.             |
| `AirFrictionComponent`              | Slows down an entity even when not collided with anything.                                            |
| `GravityComponent`                  | A singleton component that specifies gravity value for the game world.                                |
| `CursorAttachmentComponent`         | Attaches the entity to the mouse cursor.                                                              |
| `MagnetComponent`                   | Attracts or repels physic entities.                                                                   |
| `ChaoticMovementComponent`          | Adds a random chaotic movement to physics entities.                                                   |
| `PhysicsGridConfigurationComponent` | Singleton component that configures the physics grid used for pathfinding.                            |
| `PhysicsGridObstacleComponent`      | Marks entity as obstacle that is marked in the physics grid.                                          |
| `FluidComponent`                    | Creates a fluid that can be used to create animated fluid visuals and interact with physics entities. |

## Audio components

To use these components call `environment.enableAudio()` first.

| Component        | Description                                                                                                          |
|------------------|----------------------------------------------------------------------------------------------------------------------|
| `SoundComponent` | Adds continuous sound playback to the entity position. See [Audio](../core-modules/audio.md#using-ecs-for-playback). |

## Light components

To use these components call `environment.enableLight()` first.

| Component                     | Description                                                                                  |
|-------------------------------|----------------------------------------------------------------------------------------------|
| `ConeLightComponent`          | Adds a cone light at the entity position.                                                    |
| `PointLightComponent`         | Adds a point light at the entity position.                                                   |
| `SpotLightComponent`          | Adds a spot light at the entity position.                                                    |
| `AerialLightComponent`        | Adds a aerial light at the entity position.                                                  |
| `GlowComponent`               | Adds a glow at the entity position.                                                          |
| `OrthographicWallComponent`   | Marks entity as orthographic wall that will only be illuminated from below and cast shadows. |
| `ShadowCasterComponent`       | Adds shadow casting from the entity bounds.                                                  |
| `StaticShadowCasterComponent` | Optimizes performance by combining shadow caster components that won't move.                 |

## Particle components

To use these components call `environment.enableParticles()` first.

| Component                      | Description                                                                             |
|--------------------------------|-----------------------------------------------------------------------------------------|
| `ParticleComponent`            | Marks an entity as particle. Will be automatically added to all particles.              |
| `ParticleEmitterComponent`     | Adds particle emission to an entity.                                                    |
| `ParticleInteractionComponent` | Transmits entity motion on other entities that have `ParticleComponent` and are nearby. |
| `ParticleBurstComponent`       | Used to automatically shutdown particle emitters after a timeout.                       |