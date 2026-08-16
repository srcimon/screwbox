### 🚀 Features & improvements

- Compare objects by public field values.
- Updated entity baking mechanism now works with any component marked using `@Bakeable` annotation (#1072)
- Smoother scene starts because baking now happens within scene initialization
- Reduced savegame file size and ecs load by removing intermediate bake results (#1073)
- Enabled baking of `SmokeObstacleComponent` to improve smoke performance
- Configure smoke draw order

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