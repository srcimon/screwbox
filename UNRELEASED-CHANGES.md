### 🚀 Features & improvements

- Lock in mouse cursor within game window (#335)
- Added properties to `CircleDrawOptions` that allow drawing arcs (#474)
- Added properties to `RectangleDrawOptions` that allow drawing rounded rectangles
- Added `FADING` drawing style for rectangles
- Allow creation of `Angle` from percentage
- Added `x()`, `y()`, `maxX()` and `maxY()` to `ScreenBounds`
- Render aerial glow effects (#727)
- Automate rendering of aerial glow effects using `AerialGlowComponent`
- Use `SmoothValue` for smoother metrics retrieval

### 🪛 Bug Fixes

- Fixed `LogFpsSystem` logging zero fps when just started the engine
- Fixed unsteady lightmap when drawing rotated sprites
- Fixed exception on `FluidInteractionSystem` updating interactors without `PhysicsComponent`

### 🧽 Cleanup & refactoring

- Reduced minimum target fps to 60 and removed constant `Loop.MIN_TARGET_FPS`
- Added and used `Frame.hasIdenticalPixels(Frame)` to enhance unit test performance
- Simplified code for taking screenshots
- Added glow and light effects to lava in example platformer (#561)

### 📦 Dependency updates

- Bump AssertJ to 3.27.5
- Bump Mockito to 5.20.0
- Bump Node dependencies