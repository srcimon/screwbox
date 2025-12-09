### 🚀 Features & improvements

- Check for closest points to `Line` and `Path` using `closestPoint(Vector)`
- Added support for complex polygons: `Polygon.isClosed()`, `.isOpen()`, `.nodes()`, `.definitionNodes()`,
  `.addNode(Vector)`, `.isOrientedClockwise()`
- Calculate Angle between two lines `Angle.betweenLines(...)`
- Added new system execution order `SIMULATION_PREPARE`

### 🪛 Bug Fixes

- ...

### 🧽 Cleanup & refactoring

- Renamed `Polygon` and methods accordingly (#864)
- Renamed `Line` properties `start` and `end`
- `SoftBodyComponents` requires last entity to link back to the first one
- Usage of new helper `LazyValue` to wrap lazily calculated values

### 📦 Dependency updates

- ...