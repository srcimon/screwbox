### 🚀 Features & improvements

- Rendering smoothed polygons using spline algorithm (#806)
- `Graphics.renderTaskCount()` returns number of render tasks executed in the last frame

### 🪛 Bug Fixes

- Fixed `AutoTileSystem` not working when system is reused in an unchanged setting
- Fixed camera shake not working in split screen mode

### 🧽 Cleanup & refactoring

- Replaced cameraBounds property of `CameraBoundsComponent` with entity bounds (#787)
- Replaced smoothing flag with enum in `PolygonDrawOptions`
- Renamed `ParticleOptions` lifespan
- Renamed `Vector.limit(Double)` (#810)

### 📦 Dependency updates

- Bump JUnit to 6.0.1