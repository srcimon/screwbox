### 🚀 Features & improvements

- Optimized drawing of circles using `CircleDrawOptions` (#208)
- Optimized drawing of sprites with `SpriteDrawOptions` (#200)
- Added new component and methods for adding light glow effects
- Added `Graphics.toScreen(Bounds)`
- 
### 🪛 Bug Fixes

- ...

### 🧽 Cleanup & refactoring

- Added JavaDoc to all packages (#230)
- Increased min fps from 10 to 30 to avoid issues with physics in very low fps situations
- Replaced `LightOptions` builder pattern with simple variables for color and radius
- Replaced `RectangleDrawOptions.isFilled()` with `.style()`
- Replaced `Flip` with `SpriteDrawOptions`

### 📦 Dependency updates

- ...