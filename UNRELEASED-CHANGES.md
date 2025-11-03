### 🚀 Features & improvements

- Rendering smoothed polygons using spline algorithm (#806)
- `Graphics.renderTaskCount()` returns number of render tasks executed in the last frame
- Retrieve `currentDrawOrder` from `Environment`

### 🪛 Bug Fixes

- Fixed `AutoTileSystem` not working when system is reused in an unchanged setting
- Fixed camera shake not working in split screen mode

### 🧽 Cleanup & refactoring

- Replaced cameraBounds property of `CameraBoundsComponent` with entity bounds (#787)
- Replaced smoothing flag with enum in `PolygonDrawOptions`
- Renamed `ParticleOptions` lifespan
- Renamed `Vector.limit(Double)` (#810)
- Renamed `Order` enum and made it a standalone class
- Renamed `ExecutionOrder` annotation

### 📦 Dependency updates

- Bump JUnit to 6.0.1