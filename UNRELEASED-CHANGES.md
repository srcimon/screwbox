### 🚀 Features & improvements

- Added new system execution order `SIMULATION_PREPARE`
- Check for closest points to `Line` and `Path` using `closestPoint(Vector)`
- Check for closed polygons using `Polygon.isClosed()` and distinguish `nodes()` from `definitionNodes()`
- Add node to existing polygons using `Polygon.addNode(Vector)`
- Calculate Angle between two lines `Angle.betweenLines(...)`

### 🪛 Bug Fixes

- ...

### 🧽 Cleanup & refactoring

- Renamed `Polygon` and methods accordingly (#864)
- Renamed `Line` properties `start` and `end`
- `SoftBodyComponents` requires last entity to link back to the first one
- Usage of new helper `LazyValue` to wrap lazily calculated values

### 📦 Dependency updates

- ...