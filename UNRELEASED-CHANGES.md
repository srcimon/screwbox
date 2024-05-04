### 🚀 Features & improvements

- Added transitions to scene changes (#231)
- Added `Scenes.screenshotOfScene(Class)` (#313)
- Added `Screen.lastScreenshot()`
- Added `AnimationBundle` to support your scene transitions

### 🪛 Bug Fixes

- Fixed cursor change when cursor for fullscreen and window mode are changed at the same time
- Fixed uneccesary need for `PhysicsComponent` for particle interactors

### 🧽 Cleanup & refactoring

- Removed obsolete `ScreenTransitionSystem` / `Component` (#153)
- Renamed `TweenMode` to `Ease` and moved class to core package
- Moved asset bundles to the package with best matching scope

### 📦 Dependency updates

- ...