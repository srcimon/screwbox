# Components overview

An overview over all components shipped with the [ScrewBox ECS](../fundamentals/ecs.md)
Adding a `Component` to an `Entity` will automatically add the described behaviour if the corresponding `EntitySystem` was added to the `Environment`.
See [Prepacked systems and components](../fundamentals/ecs.md#prepacked-systems-and-components) for more information.

## AI components

To use these components call `environment.enableAi()` first.

| Component                 | Description                                                                |
|---------------------------|----------------------------------------------------------------------------|
| `PatrolMovemenComponent`  | Applies a patrolling movement pattern.                                     |
| `PathMovementComponent`   | Moves an entity along a `Path`.                                            |
| `TargetMovementComponent` | Moves the entity towards the specified position. Does not avoid obstacles. |

## Tweening components

To use these components call `environment.enableTweening()` first.

| Component                     | Description                                                            |
|-------------------------------|------------------------------------------------------------------------|
| `TweenComponent`              | Adds tweening to an entity. Required for all tween related components. |
| `TweenPositionComponent`      | Adds tweening movement from one position to another.                   |
| `TweenOrbitPositionComponent` | Adds an orbiting movement around a central position.                   |
| `TweenDestroyComponent`       | Removes the entity after the tween is finished.                        |
| `TweenScaleComponent`         | Tweens the scale of an `Sprite` used in the `RenderComponent`.         |
| `TweenOpacityComponent`       | Tweens the opacity of an `Sprite` used in the `RenderComponent`.       |
| `TweenSpinComponent`          | Tweens the spin `Sprite` used in the `RenderComponent`.                |
| `TweenLightSystem`            | Tweens the opacity of all light related components.                    |

## Rendering components

To use these components call `environment.enableRendering()` first.

| Component                   | Description                                                            |
|-----------------------------|------------------------------------------------------------------------|
| `RenderComponent`           | Renders a `Sprite` at the entity position.                             |
| `ReflectionComponent`       | Reflects sprites at the entity bounds. Also supports water animations. |
| `MovementRotationComponent` | Rotates `Sprites` in direction of movement.                            |
| `FixedRotationComponent`    | Continuously updates the rendered `Sprite` rotation.                   |
| `FlipSpriteComponent`       | Flips the `Sprite` horizontally when the entity moves to the left.     |
| `FixedSpinComponent`        | Continuously updates the rendered `Sprite` spin.                       |
| `CameraTargetComponent`     | Moves the [camera](../engine-components/camera) towards the entity.    |

## Logic components

To use these components call `environment.enableLogic()` first.


| Component              | Description                                                                         |
|------------------------|-------------------------------------------------------------------------------------|
| `TriggerAreaComponent` | Detects when the `Entity` collides with another matching the specified `Archetype`. |
| `StateComponent`       | Used to add a simple state machine to the entity.                                   |
