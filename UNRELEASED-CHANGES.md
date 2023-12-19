## v1.0.0-RC4

### 🚀 Features & improvements

- Updated project structure to prevent example projects from being released (#160)
- Added name property to `Entity` for debug purpose, also included in `Entity.toString()` (#142)
- Added `Environment.enable...`-methods to quickly setup an environment (#129)
- Added `Environment.removeSystemIfPresent(systemType)`
- Improved performance of `Line.intersects(line)` and `Raycast.nearestHit()`

### 🪛 Bug Fixes

- Fixed `GravitySystem` crashes when no `GravityComponent` present 

### 🧽 Cleanup & refactoring

- Renamed `Async.runSingle` to `.runExclusive`
- Removed `Light.shadowCasters()`
- Removed unused `RegisterToSignalSystem`

### 📦 Dependency updates

- ...