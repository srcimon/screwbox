### 🚀 Features & improvements

- Added `SmoothValue` for smoother metrics retrieval
- Added properties to `CircleDrawOptions` that allow drawing arcs (#474)
- Added properties to `RectangleDrawOptions` that allow drawing rounded rectangles
- Added `FADING` drawing style for rectangles
- Allow creation of `Angle` from percentage
- Lock in mouse cursor within game window (#335)
- Added `x()`, `y()`, `maxX()` and `maxY()` to `ScreenBounds`

### 🪛 Bug Fixes

- Fixed `LogFpsSystem` logging zero fps when just started the engine
- Fixed unsteady lightmap when drawing rotated sprites
- Updated critical fps count up to 40 to prevent physics glitches

### 🧽 Cleanup & refactoring

- Reduced minimum target fps to 60 and removed constant `Loop.MIN_TARGET_FPS`
- Added and used `Frame.hasIdenticalPixels(Frame)` to enhance unit test performance
- Simplified code for taking screenshots

### 📦 Dependency updates

- Bump AssertJ to 3.27.5
- Bump Mockito to 5.20.0
- Bump Node dependencies