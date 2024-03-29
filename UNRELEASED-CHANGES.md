### 🚀 Features & improvements

- Optimized drawing of texts using `TextDrawOptions` (#207)
- Added utility methods for fast access transform information of entity: `.position()`, `.origin()`, `.bounds()` and `.moveTo()` (#202)
- Added `Audio.microphoneLevel()` and `.isMicrophoneActive()` (#256)
- Added `Environment.removeAllComponentsOfType(Class)`
- Added `Duration.seconds()`
- Added `Time.add(Long, Unit)`
- Added `Duration.humanReadable(#261)`

### 🪛 Bug Fixes

- Fixed wrong radius of light glows (#253)
- Fixed renderer crash when drawing rectangles using width or height of zero
- Fixed invalid lightmap blur configuration
- Replaced `Time.plus(Duration)` with `Duration.addTo(Time)` to fix circular dependency (#260)

### 🧽 Cleanup & refactoring

- No more uneccessary drawing of lightmap when ambient light is on full brightness
- Renamed `Time.addSeconds`, `.addMillis`
- Replaced `Time` constants with enum `Time.Unit`
- Removed `Font`

### 📦 Dependency updates

- ...