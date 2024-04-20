### 🚀 Features & improvements

- Added `Duration.oneSecond()`
- Added `Scenes.lastSceneScreenshot()`
- Replaced engine logo asset

### 🪛 Bug Fixes

- Fixed generation of JavaDoc for internal packages (#296)
- Fixed wrong offset in `TweenLightSystem`

### 🧽 Cleanup & refactoring

- `ChaoticMovementSystem` uses interval to apply new speed instead of fixed duration of one second
- Disabled JavaDoc generation for example apps (#297)
- Simplified setting taskbar image on MacOs

### 📦 Dependency updates

- Bump ghaction-import-gpg to v6.1.0