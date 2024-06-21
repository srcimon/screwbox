### 🚀 Features & improvements

- Added `Sprite.singleImage()`
- Added `width()` and `height()` to `Window`, `Screen`, `Frame` and `Sprite` via common interface `Sizeable`
- Added `Playback.progress(Time)`

### 🪛 Bug Fixes

- Engine cannot be stopped when it has not been startet yet
- Engine won't stop JVM process when stopped

### 🧽 Cleanup & refactoring

- Improved performance of BlurImageFilter (#330)
- Removed unecessary methods from `Window` to set cursor from `Frame`
- Moved `MouseCursor` in correct package
- Renamed `Physics.setGrid(Grid)`
- Removed unecessary `Engine.start(Class)`
- Removed unecessary `ListUtil.addAll(List, List)`

### 📦 Dependency updates

- Bump jacoco-maven-plugin to 0.8.12
- Bump maven-enforcer-plugin to 3.5.0
- Bump maven-javadoc-plugin to 3.7.0
- Bump maven-surefire-plugin to 3.2.5
- Bump nexus-staging-maven-plugin to 1.7.0
- Bump maven-release-plugin to 3.0.1