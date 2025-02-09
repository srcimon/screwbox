# Additional systems overview

A list of all entity systems shipped with ScrewBox that can be added manually.
These systems are not added when using `environment.enableAllFeatures()` because they are active directly without being triggered by a `Component`.

| System                    | Description                                                                                     |
|---------------------------|-------------------------------------------------------------------------------------------------|
| `LogFpsSystem`            | Logs the current fps every two seconds.                                                         |
| `PathMovementDebugSystem` | Adds a visualization overlay for the `PathMovementSystem` to support understanding pathfinding. |
| `CrtMonitorOverlaySystem` | Adds a CRT monitor effect.                                                                      |
| `QuitOnKeySystem`         | Stops the game when the quit key is pressed.                                                    |
| `ParticleDebugSystem`     | Adds a visualization overlay for particle emitters.                                             |