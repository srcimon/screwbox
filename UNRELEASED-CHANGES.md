### 🚀 Features & improvements

- Added `Audio.microphoneLevel()` and `.isMicrophoneActive()` (#256)
- Added `Environment.removeAllComponentsOfType(Class)`
- Added utility methods for fast access transform information of entity: `.position()`, `.origin()`, `.bounds()` and `.moveTo()` (#202)
- Added `Duration.seconds()`

### 🪛 Bug Fixes

- Fixed wrong radius of light glows (#253)
- Fixed renderer crash when drawing rectangles using width or height of zero
- Replaced `Time.plus(Duration)` with `Duration.addTo(Time)` to fix circular dependency (#260)

### 🧽 Cleanup & refactoring

- No more uneccessary drawing of lightmap when ambient light is on full brightness

### 📦 Dependency updates

- ...