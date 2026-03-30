### 🚀 Features & improvements

- Adds collisions to the soft body boundary using `SoftBodyBoundaryComponent`
- Reworked scene transitions using post processing (#976)
- Added `Bounds.corners()` and `.borders()`
- Added `Scene.switchTime()`

### 🪛 Bug Fixes

- Fixed logging low fps when switching scenes

### 🧽 Cleanup & refactoring

- Added `SoftPhysicsSupport.toPolygon(List<Entity>)`
- Fixed inconsistent naming of `Environment.add(...)`
- Refactored common code to `PostProcessingFilter.drawImageSource(...)`
- Removed no longer necessary `RenderSceneTransitionSystem`

### 📦 Dependency updates

- Bump Node to 11.12.1
- Bump Node dependencies
- Bump Jackson to 2.21.2