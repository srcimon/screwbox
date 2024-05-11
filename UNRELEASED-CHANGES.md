### 🚀 Features & improvements

- Added a lot of new sprites sponsored by @myTrx to `SpriteBundle` (#321)
- Added `Entity.moveBy(Vector)`
- Added `SpriteBatch.isEmpty()`
- Added `Sprite.pixel(Color)`
- Added `Sprite.animatedAssetFromFile(...)`

### 🪛 Bug Fixes

- Fixed graphics glitch when reflection intersects upper edge of screen (#317)
- Fixed resetting extro ease and duration when specifying extro animation
- Fixed opacity of glow is too high

### 🧽 Cleanup & refactoring

- Added renderer decorator to block all uneccessary or potentially harmfull drawing calls (#259)
- Enhanced log entry on engine stop
- Removed api for drawing sprites using clip
- `CollisionCheck` now uses performance optimized methods for detecting the entity bounds
- Renamed existing sprite assets

### 📦 Dependency updates

- ...