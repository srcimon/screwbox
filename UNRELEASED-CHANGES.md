### 🚀 Features & improvements

- Added `ScreenBounds.move(int,int)`

### 🪛 Bug Fixes

- Fixed drawing polygons in split screen mode

### 🧽 Cleanup & refactoring

- Added high performance opacity inversion (takes 2% of the time of the old implementation)
- Heavily improved light rendering performance due to improved opacity inversion speed (#925)
- Improved overall rendering performance due to reused transforms
- Improved overall garbage collection load

### 📦 Dependency updates

- ...