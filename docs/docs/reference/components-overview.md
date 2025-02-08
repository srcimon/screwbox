# Components overview

An overview over all components shipped with the [ScrewBox ECS](../fundamentals/ecs.md)
Adding a `Component` to an `Entity` will automatically add the described behaviour.

## AI components

AI related components.
To use these components call `environment.enableAi()` first.

| Component                 | Description                                                                |
|---------------------------|----------------------------------------------------------------------------|
| `PatrolMovemenComponent`  | Applies a patrolling movement pattern.                                     |
| `PathMovementComponent`   | Moves an entity along a `Path`.                                            |
| `TargetMovementComponent` | Moves the entity towards the specified position. Does not avoid obstacles. |
