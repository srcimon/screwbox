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

To utilize these components, first call `environment.enableAi()`.

| Component                 | Description                                                                       |
|---------------------------|-----------------------------------------------------------------------------------|
| `PatrolMovementComponent` | Applies a patrolling movement pattern.                                            |
| `TargetMovementComponent` | Moves the entity towards the specified position. Does not avoid obstacles.        |
| `PathMovementComponent`   | Moves an entity along a `Path`.                                                   |
| `TargetLockComponent`     | Rotates the sprites of the `RenderComponent` towards the specified target entity. |

## Control components

To utilize these components, first call `environment.enableControls()`.

| Component                     | Description                                      |
|-------------------------------|--------------------------------------------------|
| `LeftRightControlComponent`   | Let the entity move left and right on key press. |
| `JumpControlComponent`        | Let the entity do a jump on key press.           |
| `SuspendJumpControlComponent` | Enforce ground contact for jumping.              |

## Tweening components

To utilize these components, first call `environment.enableTweening()`.

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

To utilize these components, first call `environment.enableRendering()`.

| Component                 | Description                                                                           |
|---------------------------|---------------------------------------------------------------------------------------|
| `RenderComponent`         | Renders a `Sprite` at the entity position.                                            |
| `AutoTileComponent`       | Enables dynamic auto tiling for the entity.                                           |
| `ReflectionComponent`     | Reflects sprites at the entity bounds. Also supports water animations.                |
| `MotionRotationComponent` | Rotates `Sprites` in direction of motion.                                             |
| `FixedRotationComponent`  | Continuously updates the rendered `Sprite` rotation.                                  |
| `FlipSpriteComponent`     | Flips the `Sprite` horizontally when the entity moves to the left.                    |
| `FixedSpinComponent`      | Continuously updates the rendered `Sprite` spin.                                      |
| `CameraTargetComponent`   | Moves the [camera](../core-modules/graphics/index.md) towards the entity.             |
| `CameraBoundsComponent`   | Sets the bounds in which the camera will be moved when using `CameraTargetComponent`. |

## Logic components

To utilize these components, first call `environment.enableLogic()`.

| Component              | Description                                                                         |
|------------------------|-------------------------------------------------------------------------------------|
| `TriggerAreaComponent` | Detects when the `Entity` collides with another matching the specified `Archetype`. |
| `StateComponent`       | Used to add a simple state machine to the entity.                                   |

## Physics components

To utilize these components, first call `environment.enablePhysics()`.

| Component                    | Description                                                                                 |
|------------------------------|---------------------------------------------------------------------------------------------|
| `PhysicsComponent`           | Adds physics behaviour to an entity. Applies friction and movement and avoids collisions.   |
| `CollisionSensorComponent`   | Detects collisions with physics.                                                            |
| `ColliderComponent`          | Prevents physic entities from intersecting.                                                 |
| `AttachmentComponent`        | Attaches an entity to another.                                                              |
| `CollisionDetailsComponent`  | Collects detailed information on collisions detected by `CollisionSensorComponent`.         |
| `StaticColliderComponent`    | Optimizes performance when added to entities with `ColliderComponent` that will not move.   |
| `GravityComponent`           | A singleton component that specifies gravity value for the game world.                      |
| `CursorAttachmentComponent`  | Attaches the entity to the mouse cursor.                                                    |
| `TailwindComponent`          | Transmits entity motion on other nearby entities that contains `TailwindPropelledComponent` |
| `TailwindPropelledComponent` | Receive acceleration by other nearby entities that contain `TailwindComponent`              |
| `MagnetComponent`            | Attracts or repels physic entities.                                                         |
| `ChaoticMovementComponent`   | Adds a random chaotic movement to physics entities.                                         |

## Flex physics components

To utilize these components, first call `environment.enableSoftPhysics()`.
Get a more inside from the [ropes guide](../guides/ropes).

| Component                | Description                                                                                    |
|--------------------------|------------------------------------------------------------------------------------------------|
| `SoftLinkComponent`      | Links one entity to another with a flexible spring. Used to create ropes and soft bodies.      |
| `SoftStructureComponent` | Links one entity to multiple others with flexible springs. Used to create flexible structures. |
| `RopeComponent`          | Used to mark the start of a rope.                                                              |
| `RopeRenderComponent`    | Renders ropes when added to entity also containing a `RopeComponent`                           |

## Navigation components

To utilize these components, first call `environment.enableNavigation()`.

| Component                   | Description                                                   |
|-----------------------------|---------------------------------------------------------------|
| `NavigationRegionComponent` | Singleton component that indexes the region for pathfinding.  |
| `ObstacleComponent`         | Marks entity as obstacle that will be avoided in pathfinding. |

## Fluid Components

To utilize these components, first call `environment.enableFluids()`.
Get a more inside from the [dynamic fluids guide](../guides/dynamic-fluids).

| Component                   | Description                                                                                           |
|-----------------------------|-------------------------------------------------------------------------------------------------------|
| `FluidComponent`            | Creates a fluid that can be used to create animated fluid visuals and interact with physics entities. |
| `FloatComponent`            | Lets physics entities float on fluids.                                                                |
| `FluidEffectsComponent`     | Adds audio and particle effects to fluids.                                                            |
| `DiveComponent`             | Lets floating physics entities dive into fluids.                                                      |
| `FluidInteractionComponent` | Will apply waves when on fluids when in contact.                                                      |
| `FluidTurbulenceComponent`  | Adds a turbulent motion to a fluid without need of physics entity interaction.                        |
| `FluidRenderComponent`      | Will render fluid when added to entity also containing `FluidComponent`.                              |
| `FloatRotationComponent`    | Adjusts rotation of `RenderComponent` to currently fluid wave.                                        |

## Audio components

To use these components call `environment.enableAudio()` first.

| Component        | Description                                                                                                          |
|------------------|----------------------------------------------------------------------------------------------------------------------|
| `SoundComponent` | Adds continuous sound playback to the entity position. See [Audio](../core-modules/audio.md#using-ecs-for-playback). |

## Light components

To use these components call `environment.enableLight()` first.

| Component                   | Description                                                                                  |
|-----------------------------|----------------------------------------------------------------------------------------------|
| `ConeLightComponent`        | Adds a cone light at the entity position.                                                    |
| `PointLightComponent`       | Adds a point light at the entity position.                                                   |
| `SpotLightComponent`        | Adds a spot light at the entity position.                                                    |
| `AreaLightComponent`        | Adds a area light at the entity position.                                                    |
| `AreaGlowComponent`         | Adds a area glow at the entity bounds.                                                       |
| `GlowComponent`             | Adds a glow at the entity position.                                                          |
| `ConeGlowComponent`         | Adds a cone glow at the entity position.                                                     |
| `OrthographicWallComponent` | Marks entity as orthographic wall that will only be illuminated from below and cast shadows. |
| `OccluderComponent`         | Adds shadow casting from the entity bounds.                                                  |
| `StaticOccluderComponent`   | Optimizes performance by combining occluder components that won't move.                      |

## Particle components

To use these components call `environment.enableParticles()` first.

| Component                  | Description                                                                |
|----------------------------|----------------------------------------------------------------------------|
| `ParticleComponent`        | Marks an entity as particle. Will be automatically added to all particles. |
| `ParticleEmitterComponent` | Adds particle emission to an entity.                                       |
| `ParticleBurstComponent`   | Used to automatically shutdown particle emitters after a timeout.          |