### 🚀 Features & improvements

- Added `Entity.moveBy(Vector)`

### 🪛 Bug Fixes

- Fixed graphics glitch when reflection intersects upper edge of screen (#317)
- Fixed resetting extro ease and duration when specifying extro animation

### 🧽 Cleanup & refactoring

- Added running time to log when stopping the engine
- Added renderer decorator to block all uneccessary or potentially harmfull drawing calls (#259)
- Removed api for drawing sprites using clip
- `CollisionCheck` now uses performance optimized methods for detecting the entity bounds

### 📦 Dependency updates

- ...