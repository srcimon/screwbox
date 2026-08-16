### 🚀 Features & improvements

- Compare objects by public field values.
- Reduced savegame file size and engine load by removing intermediate bake results

### 🪛 Bug Fixes

- Fixed smoke velocity is dependent on grid size (#1068)
- Fixed smoke velocity not adjusted when dynamically changing grid
- Fixed smoke turning off too quickly when out of visible area

### 🧽 Cleanup & refactoring

- Replaced `GeometryUtil` with `Bounds.tryMerge(Bounds)`
- Simplyfied `OptimizePhysicsPerformanceSystem` and `OptimizeLightPerformanceSystem`

### 📦 Dependency updates

- ...