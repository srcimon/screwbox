### 🚀 Features & improvements

- Added methods for drawing texts on screen using `Pixelfont.default()`
- Added `Pixelfont.widthOf(text)`
- Added warning when started app on MacOs without required JVM option to support fullscreen (#176)
- Added fileName validation to `Map.fromJson(fileName)`
- Added `Audio.playMusicLooped(Sound)`
- Added `Audio.isActive(Sound)`
- Added `Audio.mute()`
- Added volume control to the platformer example

### 🪛 Bug Fixes

- Fixed active audio is not affected by volume changes (#182)

### 🧽 Cleanup & refactoring

- Renamed `Percent.min()` and `.max()` methods
- Added Javadoc to `Percent`
- Excluded internal packages from Javadoc and added Javadoc badge
- Renamed `Path.start()` to `.firstNode()` and `.end()` to `.lastNode()`
- Made `Path` immutale

### 📦 Dependency updates

- ...