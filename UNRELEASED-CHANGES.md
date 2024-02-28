### 🚀 Features & improvements

- Optimized drawing of rectangles using `RectangleDrawOptions` (#205)
- Optimized drawing of lines using `LineDrawOptions` (##206)
- Added `Graphics.moveCameraWithinVisualBounds(Vector, Bounds)` (#210)
- Updated example applications with latest camera features
- Added `ChaoticMovementEntity` and `ChaoticMovementEntity`
- Added `Vector.nearestOf(List<Vector>)`
- Added `Pixelfont.sizeOf(String)`
- Added `Line.intersections(List<Line>)`
- Added `Window.isOpen()`
- Added `Size.allPixels()`
- Added `Frame.listPixelDifferences(Frame)`
- Added `Color.hex(String)`
### 🪛 Bug Fixes

- Fixed `Window.size()` and `Screen.size()` when window has not been opened yet
- Initial `World.visibleArea()` drastically reduced via anticipated `Screen.size()`

### 🧽 Cleanup & refactoring

- Renamed camera movement and zoom methods
- Moved screenshot taking from `Renderer` to `Screen`
- Simplified `CameraUpdateSystem`
- Added better error message when trying to take screenshot before window is opened
- Removed frame dependencies from renderer to enable render to draw on any graphics

### 📦 Dependency updates

- ...