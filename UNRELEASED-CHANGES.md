### 🚀 Features & improvements

- Added new post filter for water effects created via `ReflectionComponent`. (#453)
- Added configuration properties to `ReflectionComponent` to configure speed, amplitude and frequency of water effect.
- Added `Raycast.nearestEntity()`
- Change game speed via `Loop.setSpeed(Double)`
- Collect further information on collisions via `CollisionInfoComponent`

### 🪛 Bug Fixes

- Fixed NullPointerException when stopping audio that has no adressed line yet.

### 🧽 Cleanup & refactoring

- Improved performance of `CollisionSensorSystem`
- Renamed `CollisionSensorComponent` / `System`

### 📦 Dependency updates

- ...