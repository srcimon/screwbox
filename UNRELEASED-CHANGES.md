This upgrade is huge for the internals of ScrewBox.

### 🚀 Features & improvements

- Completly reworked the rendering systems. Rendering now happens on the `Canvas` and not on the `Screen`.
This adds some flexibility when setting the actual rendering area.
- Added `Viewport` class to do all the calculation bewtween `Canvas` and `Screen`. (#404)
- Added `Rotation.invert()`

### 🪛 Bug Fixes

- Fixed wrong info in JavaDoc

### 🧽 Cleanup & refactoring

- Combined `ReflectionRenderSystem` and `RenderSystem` to avoid the performance implications and complexity off adding and removing entities to communicate between both systems.
- Reduced lightmap size and used optimzed blur filter to slightly reduce memory transfer rate (#393)
- Moved all draw options to dedicated package
- Simplified renderer interface
- Added proxy renderer reduce cuppling between window and screen
- Renamed lots of methods translating `Screen`, `Canvas` and `World` positions.

### 📦 Dependency updates

- ...