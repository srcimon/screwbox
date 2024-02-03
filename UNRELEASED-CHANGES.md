### 🚀 Features & improvements

- Added methods for drawing texts on screen using `Pixelfont.default()`
- Added `Pixelfont.widthOf(text)`
- Added warning when started app on MacOs without required JVM option to support fullscreen (#176)
- Added fileName validation to `Map.fromJson(fileName)`
- Added `Audio.playMusicLooped(Sound)`
- Added `Audio.isActive(Sound)`
- Added `Sound.duration()` (#189)
- Added muting and unmuting of audio. (#181)
- Added volume control to the platformer example
- Reduced latency when playing sounds
- Nicer looking loading animation

### 🪛 Bug Fixes

- Fixed active audio is not affected by volume changes (#182)
- Added minimum warm up time of 0.5 seconds to avoid flickering loading animation

### 🧽 Cleanup & refactoring

- Renamed `Percent.min()` and `.max()` methods
- Added Javadoc to `Percent`
- Excluded internal packages from Javadoc and added Javadoc badge
- Renamed `Path.start()` to `.firstNode()` and `.end()` to `.lastNode()`
- Made `Path` immutale

### 📦 Dependency updates

- Bump github actions to v4
- Bump AssertJ to 3.24.2
- Bump Mockito to 5.9.0
- Bump Jackson to 2.16.1