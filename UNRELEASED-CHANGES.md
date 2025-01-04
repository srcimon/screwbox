### 🚀 Features & improvements

- Added new post filter for water effects created via `ReflectionComponent` (#453)
- Added configuration properties to `ReflectionComponent` to configure speed, amplitude and frequency of water effect
- Added `Raycast.nearestEntity()`
- Pause or change game speed via `Loop.setSpeed(Double)`
- Collect further information on collisions via `CollisionInfoComponent`
- Use aliases to configure keyboard controlls (#466)

### 🪛 Bug Fixes

- Fixed NPE when stopping audio that has not startet yet

### 🧽 Cleanup & refactoring

- Improved performance of `CollisionSensorSystem`
- Renamed `CollisionSensorComponent` / `System`
- Added initializer constructor to `TrippleLatch`

### 📦 Dependency updates

- Bump AssertJ to 3.27.2
- Bump Mockito to 5.15.2