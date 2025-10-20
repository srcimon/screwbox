### 🚀 Features & improvements

- Completely reworked pathfinding api (#780)
- Fast UUID creation using `FastRandom.createUUID()`
- Added `Async.hasNoActiveTask(Object)`
- Expand Bounds to containing grid cells
- Translate positions within a default grid using `Grid.findCell(Vector, int)`

### 🪛 Bug Fixes

- Fixed name of `engine.navigation()`

### 🧽 Cleanup & refactoring

- Significantly improved performance of spawning asynchronous tasks
- Improved pathfinding performance on grids up to 40k nodes (#770)
- Pathfinding is no longer directly dependent on `Grid`(#765)
- `Grid` no longer stores `useDiagonalMovement` boolean (#764)
- Renamed `Grid.cellSize()`
- Added missing `Path.toString()`

### 📦 Dependency updates

- Bump Docusaurus to 3.9.2
- Bump maven-javadoc-plugin to 3.12.0
- Bump jacoco-maven-plugin to 0.8.14
- Bump sonar-maven-plugin to 5.2.0.4988
- Bump central-publishing-maven-plugin to 0.9.0