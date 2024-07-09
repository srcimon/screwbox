### 🚀 Features & improvements

- Added `Scenes.setDefaultTransition(SceneTransition)`
- Added `Scenes.resetActiveScene()` and `.resetActiveScene(SceneTransition)`
- Added `Keyboard.isAnyKeyPressed()`

### 🪛 Bug Fixes

- Fixed resetted attributes in `SceneTransition` when setting intro animation after outro animation
- Fixed outro animation is drawn on loading scene
- Fixed engine shutdown on exception before even starting
- Fixed `Window.filesDropedOnWindow()` is empty when file is dropped between updates

### 🧽 Cleanup & refactoring

- Made `SoundOptions` immutable
- Renamed `Scenes.exist(Class)`
- Specified version of sonar-maven-plugin
- Made return value of `Pixelfont.spriteFor(Character)` optional

### 📦 Dependency updates

- ...