### 🚀 Features & improvements

- Added parallax support to `RenderSystem` (#277)
- Improved reflections now use drawOrder and support blur effect (#303)
- Added individual app icons for MacOs and Windows / Linux
- Tiled layers now uses offset when calculating sprite positions 
- Added `Size.isValid()`
- Added `Frame.colors()`
- Added `Environment.removeAll(Archetype)`
- Added `SpriteDrawOptions.invertVerticalFlip()`
- Added `ParticleOptions.startOpacity(Percent)`

### 🪛 Bug Fixes

- Fix taskbar icon on MacOs
- Fixed inconsistent initial zoom in world and camera

### 🧽 Cleanup & refactoring

- Reworked sprite batches (#304)
- Renamed asset bundles
- Added `Pixelperfect` util for pixelperfect drawing
- Changed default zoom range from 1 to 5
- Reduced default particle spawn distance to 1000

### 📦 Dependency updates

- ...