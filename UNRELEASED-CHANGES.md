### 🚀 Features & improvements

- Pick random position from bounds
- Use avoidance during pathfinding

### 🪛 Bug Fixes

- Fixed missing draw order when drawing textured ropes
- Tile maps created from image now contain transparent tiles
- Fixed inaccurate acceleration when using target movement
- Fixed wrong diagonal movement speeds when using movement controls
- Fixed cost calculation in A-Star-algorithm

### 🧽 Cleanup & refactoring

- `ListUtil.pickRandom(List)` never returns null and instead validates list not to be empty
- Slightly improved performance for updating pathfinding grid

### 📦 Dependency updates

- Bump No de dependencies