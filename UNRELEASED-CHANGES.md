### 🚀 Features & improvements

- Optimized drawing of texts using `TextDrawOptions` (#207)
- Added utility methods for fast access transform information of entity: `.position()`, `.origin()`, `.bounds()` and `.moveTo()` (#202)
- Added `Audio.microphoneLevel()` and `.isMicrophoneActive()` (#256)
- Added `Environment.removeAllComponentsOfType(Class)`
- Added `Duration.seconds()`
- Added `Time.add(Long, Unit)`
- Added `Duration.humanReadable(#261)`
- Added seperate options for x- and y-strength to `CameraShakeOptions`

### 🪛 Bug Fixes

- Fixed wrong radius of light glows (#253)
- Fixed renderer crash when drawing rectangles using width or height of zero
- Fixed invalid lightmap blur configuration
- Fixed lightmap has no more ugly corners
- Replaced `Time.plus(Duration)` with `Duration.addTo(Time)` to fix circular dependency (#260)
- Removed Thread.sleep before fullscreen changes on MacOs because  the workaround is no longer needed in Sonoma 14.4

### 🧽 Cleanup & refactoring

- No more uneccessary drawing of lightmap when ambient light is on full brightness
- Renamed `Time.addSeconds`, `.addMillis`
- Replaced `Time` constants with enum `Time.Unit`
- Removed `Font`
- Added JavaDoc to `CameraShakeOptions`.
- Replaced `MathUtil.clamp` with Java 21 `Math.clamp`

### 📦 Dependency updates

- ...