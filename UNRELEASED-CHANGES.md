### 🚀 Features & improvements

- Added new post filter for water effects created via `ReflectionComponent` (#453)
- Configure speed, amplitude and frequency of water effect in `ReflectionComponent`
- Added `Raycast.nearestEntity()`
- Pause or change game speed via `Loop.setSpeed(Double)`
- Collect further information on collisions via `CollisionInfoComponent` (#462)
- Use aliases to configure keyboard controlls (#466)
- Import simple maps directly from text via `AsciiMap`

### 🪛 Bug Fixes

- Fixed NPE when stopping audio that has not startet yet

### 🧽 Cleanup & refactoring

- Improved performance of `CollisionSensorSystem`
- Renamed `CollisionSensorComponent` / `System`
- Added initializer constructor to `TrippleLatch`

### 📦 Dependency updates

- Bump AssertJ to 3.27.2
- Bump Mockito to 5.15.2