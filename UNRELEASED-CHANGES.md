### 🚀 Features & improvements

- Added new post filter for water effects created via `ReflectionComponent` (#453)
- Configure speed, amplitude and frequency of water effect in `ReflectionComponent`
- Pause or change game speed via `Loop.setSpeed(Double)`
- Collect detailed information on collisions via `CollisionInfoComponent` (#462)
- Use aliases (enum of choice) to configure keyboard controls (#466)
- Import simple maps directly from text via `AsciiMap`
- Added `Entity.addOrReplace(Component)`
- Added `Raycast.nearestEntity()`

### 🪛 Bug Fixes

- Fixed NPE when stopping audio that has not startet yet

### 🧽 Cleanup & refactoring

- Improved performance of `CollisionSensorSystem`
- Renamed `CollisionSensorComponent` / `System`
- Added initializer constructor to `TrippleLatch`
- Renamed `Loop.time()`

### 📦 Dependency updates

- Bump AssertJ to 3.27.2
- Bump Mockito to 5.15.2