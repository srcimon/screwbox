### 🚀 Features & improvements

- Completly reworked the rendering systems. Rendering now happens on the `Canvas` and not on the `Screen`.
This adds some flexibility when setting the actual rendering area.
- Added `Rotation.invert()`

### 🪛 Bug Fixes

- Fixed wrong info in JavaDoc

### 🧽 Cleanup & refactoring

- Reduced lightmap size and used optimzed blur filter to slightly reduce memory transfer rate (#393)
- Moved all draw options to dedicated package
- Simplified renderer interface
- Added proxy renderer reduce cuppling between window and screen
- Renamed lots of methods translating `Screen`, `Canvas` and `World` positions.

### 📦 Dependency updates

- ...