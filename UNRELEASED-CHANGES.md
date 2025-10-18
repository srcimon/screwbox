### 🚀 Features & improvements

- Completely reworked pathfinding api (#780)
- Fast UUID creation using `FastRandom.createUUID()`
- Added `Async.hasNoActiveTask(Object)`
- Expand Bounds to containing grid cells

### 🪛 Bug Fixes

- Fixed name of `engine.navigation()`

### 🧽 Cleanup & refactoring

- Significantly improved performance of spawning asynchronous tasks
- Improved pathfinding performance on grids up to 40k nodes (#770)
- Pathfinding is no longer directly dependent on `Grid`(#765)
- `Grid` no longer stores `useDiagonalMovement` boolean (#764)
- Renamed `Grid.cellSize()`

### 📦 Dependency updates

- ...