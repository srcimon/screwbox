### 🚀 Features & improvements

- Fast UUID creation using `FastRandom.createUUID()`

### 🪛 Bug Fixes

- Fixed name of `engine.navigation()`

### 🧽 Cleanup & refactoring

- Improved performance of spawning async tasks due to reduced UUID creation times
- Pathfinding is no longer directly dependent on `Grid`(#765)
- `Grid` no longer stores `useDiagonalMovement` boolean (#764)
- Renamed `Grid.cellSize()`

### 📦 Dependency updates

- ...