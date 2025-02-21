---
sidebar_position: 3
---

# Standalone Systems

A list of all entity systems shipped with ScrewBox that can be added manually.
These systems are not added when using `environment.enableAllFeatures()` because they are active directly without being triggered by a `Component`.

| System                    | Description                                                                                     |
|---------------------------|-------------------------------------------------------------------------------------------------|
| `EngineWatermarkSystem`   | Adds a watermark to the bottom of the screen which shows current engine version.                |
| `CrtMonitorOverlaySystem` | Adds a CRT monitor effect.                                                                      |
| `LogFpsSystem`            | Logs the current fps every two seconds.                                                         |
| `ParticleDebugSystem`     | Adds a visualization overlay for particle emitters.                                             |
| `PathMovementDebugSystem` | Adds a visualization overlay for the `PathMovementSystem` to support understanding pathfinding. |
| `QuitOnKeySystem`         | Stops the game when the quit key is pressed.                                                    |
