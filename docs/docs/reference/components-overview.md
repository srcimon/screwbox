# Components overview

An overview over all components shipped with the [ScrewBox ECS](../fundamentals/ecs.md)
Adding a `Component` to an `Entity` will automatically add the described behaviour if the corresponding `EntitySystem` was added to the `Environment`.
See [Prepacked systems and components](../fundamentals/ecs.md#prepacked-systems-and-components) for more information.

## AI components

AI related components.
To use these components call `environment.enableAi()` first.

| Component                 | Description                                                                |
|---------------------------|----------------------------------------------------------------------------|
| `PatrolMovemenComponent`  | Applies a patrolling movement pattern.                                     |
| `PathMovementComponent`   | Moves an entity along a `Path`.                                            |
| `TargetMovementComponent` | Moves the entity towards the specified position. Does not avoid obstacles. |

## Tweening components

Tweening related components.
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
