### 🚀 Features & improvements

- Compare objects by public field values.
- Moved entity baking from dedicated systems into scene initialization to avoid volatile scene starts
- Reduced savegame file size and ecs load by removing intermediate bake results
- Enabled baking of `SmokeObstacleComponent` to improve smoke performance

### 🪛 Bug Fixes

- Fixed smoke velocity is dependent on grid size (#1068)
- Fixed smoke velocity not adjusted when dynamically changing grid
- Fixed smoke turning off too quickly when out of visible area

### 🧽 Cleanup & refactoring

- Replaced `GeometryUtil` with `Bounds.tryMerge(Bounds)`
- Replaced `OptimizeLightPerformanceSystem` and `OptimizePhysicsPerformanceSystem` with new baking mechanism
- Simplyfied `OptimizePhysicsPerformanceSystem` and `OptimizeLightPerformanceSystem`

### 📦 Dependency updates

- ...