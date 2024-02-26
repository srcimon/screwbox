### 🚀 Features & improvements

- Added `Graphics.moveCameraWithinVisualBounds(Vector, Bounds)` (#210)
- Optimized drawing of rectangles using `RectangleDrawOptions` (#205)
- Added `Vector.nearestOf(List<Vector>)`
- Added `Pixelfont.sizeOf(String)`
- Added `Line.intersections(List<Line>)`
- Updated example applications with latest camera features
- Added `Window.isOpen()`

### 🪛 Bug Fixes

- Fixed `Window.size()` and `Screen.size()` when window has not been opened yet
- Initial `World.visibleArea()` drastically reduced via anticipated `Screen.size()`

### 🧽 Cleanup & refactoring

- Renamed camera movement and zoom methods
- Moved screenshot taking from `Renderer` to `Screen`
- Simplified `CameraUpdateSystem`
- Added better error message when trying to take screenshot before window is opend

### 📦 Dependency updates

- ...