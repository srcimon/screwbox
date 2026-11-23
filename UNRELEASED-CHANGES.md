### 🚀 Features & improvements

- Pick random position from bounds
- Avoid certain areas during pathfinding
- Auto update path of `PathMovementComponent` by specifying a target id
- Use jitter to unsynchronize schedulers
- Check if environment contains entity with specified id

### 🪛 Bug Fixes

- Fixed a bug where an entity is not removed from the environment when a componend was added to the entity in the same frame
- Fixed missing draw order when drawing textured ropes
- Tile maps created from image now contain transparent tiles
- Fixed inaccurate acceleration when using target movement
- Fixed wrong diagonal movement speeds when using movement controls
- Fixed cost calculation in A-Star-algorithm

### 🧽 Cleanup & refactoring

- `ListUtil.pickRandom(List)` never returns null and instead validates list not to be empty
- Slightly improved performance for updating pathfinding grid
- Made `ObstacleComponent` bakeable
- Renamed scheduler method names consistently
- Introduced mixin interface `HasBounds` to mix in bounds related functions
- Made `SpacialIndex` and `SpacialHashRegistry` generic to query anything that `HasBounds`
- Allow double values for `LineDrawOptions.strokeWidth()`

### 📦 Dependency updates

- Bump Node dependencies
- Bump actions/upload-artifact to v7